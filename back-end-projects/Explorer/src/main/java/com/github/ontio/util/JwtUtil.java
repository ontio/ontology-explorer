package com.github.ontio.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

/**
 * JAVA-JWT util
 */
@Component
@Slf4j
public class JwtUtil {

    private static String accessTokenExpireTime;

    private static String encryptJWTKey;

    @Value("${jwt.accessTokenExpireTime}")
    public void setAccessTokenExpireTime(String tokenExpireTime) {
        JwtUtil.accessTokenExpireTime = tokenExpireTime.trim();
    }

    @Value("${jwt.encryptJWTKey}")
    public void setEncryptJWTKey(String jWTKey) {
        JwtUtil.encryptJWTKey = jWTKey.trim();
    }


    public static boolean verifyToken(String token) {
        try {
            String ontId = getClaim(token, ConstantParam.JWT_LOGINID).asString();
            String secret = preSecret(ontId);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            log.info("verifytoken jwt:{}", jwt.toString());
        } catch (Exception e) {
            log.error("verifyToken error...{}", e.getMessage());
            return false;
        }
        return true;
    }


    public static Claim getClaim(String token, String claim) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(claim);
        } catch (JWTDecodeException e) {
            log.error("getClaim error...{}", e.getMessage());
            return null;
        }
    }


    public static String signToken(String ontId) {
        // ontId+JWT key
        String secret = preSecret(ontId);
        Date date = new Date(System.currentTimeMillis() + Long.parseLong(accessTokenExpireTime) * 1000);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withClaim(ConstantParam.JWT_LOGINID, ontId)
                .withExpiresAt(date)
                .sign(algorithm);
    }

    private static String preSecret(String ontId) {
        String secret = ontId + new String(Base64.getDecoder().decode(encryptJWTKey));
        return secret;
    }


}
