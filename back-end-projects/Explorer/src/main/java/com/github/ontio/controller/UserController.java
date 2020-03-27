package com.github.ontio.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.ontio.exception.ExplorerException;
import com.github.ontio.model.common.ResponseBean;
import com.github.ontio.model.dao.User;
import com.github.ontio.model.dao.UserAddress;
import com.github.ontio.model.dto.login.CallBackDto;
import com.github.ontio.model.dto.login.CallBackResponse;
import com.github.ontio.service.IUserService;
import com.github.ontio.util.ConstantParam;
import com.github.ontio.util.ErrorInfo;
import com.github.ontio.util.Helper;
import com.github.ontio.util.JwtUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author zhouq
 * @version 1.0
 * @date 2020/3/24
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/users/")
public class UserController {

    private final String CLASS_NAME = this.getClass().getSimpleName();

    private final IUserService userService;


    @ApiOperation(value = "Query login qrcode")
    @GetMapping(value = "/login_qrcode")
    public ResponseBean queryQrCode() {
        ResponseBean rs = userService.queryQrCode();
        return rs;
    }


    @ApiOperation(value = "Query login user info")
    @GetMapping(value = "/login_user_info")
    public ResponseBean queryLoginUserInfo(@RequestParam("code") String code) {
        ResponseBean rs = userService.queryLoginUserInfo(code);
        return rs;
    }


    @ApiOperation(value = "ONTO User login")
    @PostMapping(value = "/login")
    public CallBackResponse userLogin(@RequestBody JSONObject jsonObject) {
        log.info("###{}.{} begin...param:{}", CLASS_NAME, Helper.currentMethod(), jsonObject);
        CallBackDto callBackDto = new CallBackDto();
        callBackDto.setSigner(jsonObject.getString("signer"));
        callBackDto.setSignedTx(jsonObject.getString("signedTx"));
        CallBackDto.CallbackExtraData callbackExtraData = CallBackDto.CallbackExtraData.builder()
                .id(jsonObject.getJSONObject("extraData").getString("id"))
                .build();
        callBackDto.setExtraData(callbackExtraData);
        CallBackResponse rs = userService.login(callBackDto);
        return rs;
    }


    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = "ONT_EXP_TOKEN", value = "login token", required = true)})
    @ApiOperation(value = "Query user addresses")
    @GetMapping(value = "/addresses")
    public ResponseBean queryUserAddresses(@RequestParam("ont_id") String ontId) {
        log.info("###{}.{} begin...ontId:{}", CLASS_NAME, Helper.currentMethod(), ontId);
        checkToken(ontId);
        ResponseBean rs = userService.queryUserAddresses(ontId);
        refreshToken(ontId);
        return rs;
    }


    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = "ONT_EXP_TOKEN", value = "login token", required = true)})
    @ApiOperation(value = "Add or Update user addresses")
    @PostMapping(value = "/addresses")
    public ResponseBean addOrUpdateUserAddresses(@RequestParam("ont_id") String ontId,
                                                 @RequestBody List<UserAddress> userAddresses) {
        log.info("###{}.{} begin...ontId:{}", CLASS_NAME, Helper.currentMethod(), ontId);
        checkToken(ontId);
        ResponseBean rs = userService.addOrUpdateUserAddresses(userAddresses, ontId);
        refreshToken(ontId);
        return rs;
    }


    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = "ONT_EXP_TOKEN", value = "login token", required = true)})
    @ApiOperation(value = "Delete user address")
    @DeleteMapping(value = "/addresses")
    public ResponseBean delUserAddress(@RequestParam("ont_id") String ontId,
                                       @RequestBody JSONObject jsonObject) {
        log.info("###{}.{} begin...ontId:{}", CLASS_NAME, Helper.currentMethod(), ontId);
        checkToken(ontId);
        ResponseBean rs = userService.delUserAddress(jsonObject.getString("address"), ontId);
        refreshToken(ontId);
        return rs;
    }


    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "String", name = "ONT_EXP_TOKEN", value = "login token", required = true)})
    @ApiOperation(value = "Update user information")
    @PostMapping
    public ResponseBean updateUser(@RequestParam("ont_id") String ontId,
                                   @RequestBody User user) {
        log.info("###{}.{} begin...ontId:{}", CLASS_NAME, Helper.currentMethod(), ontId);
        checkToken(ontId);
        user.setOntId(ontId);
        ResponseBean rs = userService.updateUser(user);
        refreshToken(ontId);
        return rs;
    }


    private void checkToken(String ontId) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(ConstantParam.HTTPHEADER_TOKEN);
        if (Helper.isEmptyOrNull(token)) {
            throw new ExplorerException(ErrorInfo.TOKEN_EMPTY);
        }
        if (!JwtUtil.verifyToken(token)) {
            throw new ExplorerException(ErrorInfo.TOKEN_EXPIRED);
        } else if (!JwtUtil.getClaim(token, ConstantParam.JWT_LOGINID).asString().equals(ontId)) {
            throw new ExplorerException(ErrorInfo.TOKEN_UNMATCH);
        }
    }

    private void refreshToken(String ontId) {
        String newToken = JwtUtil.signToken(ontId);
        HttpServletResponse resp = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        resp.setHeader(ConstantParam.HTTPHEADER_TOKEN, newToken);
    }

}
