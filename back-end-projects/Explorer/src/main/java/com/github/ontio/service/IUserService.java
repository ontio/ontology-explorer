package com.github.ontio.service;

import com.github.ontio.model.common.ResponseBean;
import com.github.ontio.model.dao.User;
import com.github.ontio.model.dao.UserAddress;
import com.github.ontio.model.dto.login.CallBackDto;
import com.github.ontio.model.dto.login.CallBackResponse;
import com.github.ontio.model.dto.login.QrCodeDto;

import java.util.List;

/**
 * @author zhouq
 * @version 1.0
 * @date 2020/3/24
 */
public interface IUserService {

    ResponseBean queryWebQrCode();

    QrCodeDto queryQrCode(String qrcodeId);

    ResponseBean queryLoginUserInfo(String code);

    CallBackResponse login(CallBackDto callBackDto);

    ResponseBean queryUserAddresses(String ontId);

    ResponseBean addOrUpdateUserAddresses(List<UserAddress> userAddresses, String ontId);

    ResponseBean delUserAddress(String address, String ontId);

    ResponseBean addUserAddress(UserAddress userAddresses, String ontId);

    ResponseBean updateUserAddress(UserAddress userAddresses, String ontId);

    ResponseBean addOrUpdateUser(User user);

    ResponseBean queryUserInfo(String ontId);


}
