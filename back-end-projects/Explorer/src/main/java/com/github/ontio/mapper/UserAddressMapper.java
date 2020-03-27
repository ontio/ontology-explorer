package com.github.ontio.mapper;

import com.github.ontio.model.dao.UserAddress;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserAddressMapper extends Mapper<UserAddress> {

    int saveUserAddress(List<UserAddress> userAddresses);

}