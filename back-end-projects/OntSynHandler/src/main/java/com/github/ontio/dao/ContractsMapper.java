package com.github.ontio.dao;

import com.github.ontio.model.Contracts;
import org.springframework.stereotype.Component;
import java.util.List;


@Component
public interface ContractsMapper {
    int insert(Contracts record);

    int insertSelective(Contracts record);

    Contracts selectContractByContractHash(String contract);

    int updateByPrimaryKeySelective(Contracts record);

    int updateByPrimaryKey(Contracts record);
}