package com.github.ontio.mapper;

import com.github.ontio.model.dao.UserAddress;
import com.github.ontio.txPush.model.PushUserAddressInfoDto;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserAddressMapper extends Mapper<UserAddress> {

    List<PushUserAddressInfoDto> selectUserAddressInfo();

}