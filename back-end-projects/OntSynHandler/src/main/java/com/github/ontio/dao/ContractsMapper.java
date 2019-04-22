package com.github.ontio.dao;

import com.github.ontio.model.Contracts;
import org.springframework.stereotype.Component;


@Component
public interface ContractsMapper {
    int insert(Contracts record);

    int insertSelective(Contracts record);

    Integer selectCountByContractHash(String contract);

    int updateByPrimaryKeySelective(Contracts record);

    int updateByPrimaryKey(Contracts record);
}