package com.github.ontio.service;

import com.github.ontio.model.common.ResponseBean;
import com.github.ontio.model.dao.User;
import com.github.ontio.model.dao.UserAddress;
import com.github.ontio.model.dto.login.CallBackDto;
import com.github.ontio.model.dto.login.CallBackResponse;

import java.util.List;

/**
 * @author zhouq
 * @version 1.0
 * @date 2020/3/24
 */
public interface IUserService {

    ResponseBean queryQrCode();

    ResponseBean queryLoginUserInfo(String code);

    CallBackResponse login(CallBackDto callBackDto);

    ResponseBean queryUserAddresses(String ontId);

    ResponseBean addOrUpdateUserAddresses(List<UserAddress> userAddresses, String ontId);

    ResponseBean delUserAddress(String address, String ontId);

    ResponseBean updateUser(User user);

}
