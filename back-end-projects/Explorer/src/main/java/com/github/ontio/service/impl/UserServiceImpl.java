package com.github.ontio.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.ontio.config.ParamsConfig;
import com.github.ontio.core.asset.Sig;
import com.github.ontio.core.transaction.Transaction;
import com.github.ontio.crypto.Digest;
import com.github.ontio.exception.ExplorerException;
import com.github.ontio.mapper.AddressBlacklistMapper;
import com.github.ontio.mapper.UserAddressMapper;
import com.github.ontio.mapper.UserMapper;
import com.github.ontio.model.common.PageResponseBean;
import com.github.ontio.model.common.RedisKey;
import com.github.ontio.model.common.ResponseBean;
import com.github.ontio.model.dao.AddressBlacklist;
import com.github.ontio.model.dao.User;
import com.github.ontio.model.dao.UserAddress;
import com.github.ontio.model.dto.login.CallBackDto;
import com.github.ontio.model.dto.login.CallBackResponse;
import com.github.ontio.model.dto.login.QrCodeDto;
import com.github.ontio.service.IUserService;
import com.github.ontio.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author zhouq
 * @version 1.0
 * @date 2020/3/24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private static final String CACHEKEY_BLACKADDR = "blackAddress";
    private static final String SPILT_CHAR = ",";

    private final UserAddressMapper userAddressMapper;
    private final UserMapper userMapper;
    private final ParamsConfig paramsConfig;
    private final StringRedisTemplate redisTemplate;
    private final AddressBlacklistMapper addressBlacklistMapper;
    private final OntologySDKService sdkService;

    private LoadingCache<String, List<String>> blackAddressCache = null;

    @Autowired
    private void setCache() {
        blackAddressCache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .build(key -> {
                    List<AddressBlacklist> addressBlacklist = addressBlacklistMapper.selectAll();
                    return addressBlacklist.stream().map(item -> item.getAddress()).collect(Collectors.toList());
                });
    }

    @Override
    public ResponseBean queryQrCode() {
        String code = Helper.generateLoginCode();
        redisTemplate.opsForValue().set(String.format(RedisKey.USERLOGIN_CODE, code), "", 2, TimeUnit.MINUTES);
        QrCodeDto qrCodeDto = generateQrCode(code);
        return Helper.successResult(qrCodeDto);
    }

    private QrCodeDto generateQrCode(String code) {
        QrCodeDto.QrCodeDataParamFunArg qrCodeDataParamFunArg = QrCodeDto.QrCodeDataParamFunArg.builder()
                .name("message")
                .value("")
                .build();
        QrCodeDto.QrCodeDataParamFun qrCodeDataParamFun = QrCodeDto.QrCodeDataParamFun.builder()
                .operation("signMessage")
                .args(Arrays.asList(qrCodeDataParamFunArg))
                .build();
        QrCodeDto.QrCodeDataParamDetail qrCodeDataParamDetail = QrCodeDto.QrCodeDataParamDetail.builder()
                .contractHash("0000000000000000000000000000000000000000")
                .payer("")
                .gasLimit(new BigDecimal("0"))
                .gasPrice(new BigDecimal("0"))
                .functions(Arrays.asList(qrCodeDataParamFun))
                .build();
        QrCodeDto.QrCodeDataParam qrCodeDataParam = QrCodeDto.QrCodeDataParam.builder()
                .invokeConfig(qrCodeDataParamDetail)
                .build();
        QrCodeDto.QrCodeData qrCodeData = QrCodeDto.QrCodeData.builder()
                .action("signMessage")
                .params(qrCodeDataParam)
                .build();
        String signature = sdkService.signData2HexStr(JacksonUtil.beanToJSonStr(qrCodeData));

        return QrCodeDto.testNetLoginQrCode(code, paramsConfig.IDENTITY_ONTID, signature, qrCodeData, paramsConfig.loginCallbackUrl, System.currentTimeMillis() + 2 * 60 * 1000L);
    }


    @Override
    public ResponseBean queryLoginUserInfo(String code) {
        String token = redisTemplate.opsForValue().get(String.format(RedisKey.USERLOGIN_CODE, code));
        if (token == null) {
            throw new ExplorerException(ErrorInfo.QRCODE_EXPIRED);
        } else if ("".equals(token)) {
            throw new ExplorerException(ErrorInfo.NO_LOGIN_USERINFO);
        }
        String ontId = JwtUtil.getClaim(token, ConstantParam.JWT_LOGINID).asString();
        User user = userMapper.selectByPrimaryKey(ontId);
        //set token
        HttpServletResponse resp = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        resp.setHeader(ConstantParam.HTTPHEADER_TOKEN, token);
        return Helper.successResult(user);
    }


    @Override
    public CallBackResponse login(CallBackDto callBackDto) {
        String ontId = callBackDto.getSigner();
        String signedTx = callBackDto.getSignedTx();
        String code = callBackDto.getExtraData().getId();
        Boolean verifyFlag = verifySignature(ontId, signedTx);
        if (!verifyFlag) {
            return CallBackResponse.errorResponse(ErrorInfo.VERIFY_SIGN_FAILED.code());
        }
        String redisValue = redisTemplate.opsForValue().get(String.format(RedisKey.USERLOGIN_CODE, code));
        if (redisValue == null) {
            return CallBackResponse.errorResponse(ErrorInfo.QRCODE_EXPIRED.code());
        }
        String token = JwtUtil.signToken(ontId);
        redisTemplate.opsForValue().set(String.format(RedisKey.USERLOGIN_CODE, code), token, 60, TimeUnit.SECONDS);

        Date now = new Date();
        User user = userMapper.selectByPrimaryKey(ontId);
        if (user == null) {
            user = User.builder()
                    .ontId(ontId)
                    .userName("")
                    .email("")
                    .lastLoginTime(now)
                    .createTime(now)
                    .build();
            userMapper.insert(user);
        } else {
            user.setLastLoginTime(now);
            userMapper.updateByPrimaryKeySelective(user);
        }
        return CallBackResponse.successResponse(new JSONObject());
    }

    private boolean verifySignature(String ontId, String signedTx) {
        Transaction transaction = null;
        try {
            transaction = Transaction.deserializeFrom(com.github.ontio.common.Helper.hexToBytes(signedTx));
        } catch (IOException e) {
            throw new ExplorerException(ErrorInfo.TX_ERROR);
        }
        byte[] sigBytes = transaction.sigs[0].sigData[0];
        String signature = com.github.ontio.common.Helper.toHexString(sigBytes);
        if (!signature.startsWith("01")) {
            signature = String.format("01%s", signature);
        }
        transaction.sigs = new Sig[0];
        String hex = transaction.toHexString();
        String tx = hex.substring(0, hex.length() - 2);
        String data = com.github.ontio.common.Helper.toHexString(Digest.hash256(com.github.ontio.common.Helper.hexToBytes(tx)));
        return sdkService.verifySignature(ontId, data, signature);
    }


    @Override
    public ResponseBean queryUserAddresses(String ontId) {
        UserAddress userAddress = UserAddress.builder()
                .ontId(ontId)
                .build();
        List<UserAddress> userAddresses = userAddressMapper.select(userAddress);
        PageResponseBean pageResponseBean = new PageResponseBean(userAddresses, userAddresses.size());
        return Helper.successResult(pageResponseBean);
    }


    @Override
    public ResponseBean addOrUpdateUserAddresses(List<UserAddress> userAddressList, String ontId) {
        if (userAddressList.size() > 5) {
            //normal response can refresh token
            return Helper.errorResult(ErrorInfo.ADDRESS_TOOMANY, false);
        }
        if (CollectionUtils.containsAny(blackAddressCache.get(CACHEKEY_BLACKADDR), userAddressList.stream().map(key -> key.getAddress()).collect(Collectors.toList()))) {
            return Helper.errorResult(ErrorInfo.IN_BLACKADDRESS, false);
        }
        userAddressList.forEach(userAddress -> {
            userAddress.setOntId(ontId);
        });
        userAddressMapper.saveUserAddress(userAddressList);
        return Helper.successResult(true);
    }

    @Override
    public ResponseBean delUserAddress(String address, String ontId) {
        UserAddress userAddress = UserAddress.builder()
                .ontId(ontId)
                .address(address)
                .build();
        int count = userAddressMapper.delete(userAddress);
        if (count == 0) {
            return Helper.errorResult(ErrorInfo.ADDRESS_ONTID_UNMATCH, false);
        }
        return Helper.successResult(true);
    }


    @Override
    public ResponseBean updateUser(User user) {
        userMapper.updateByPrimaryKeySelective(user);
        return Helper.successResult(true);
    }
}
