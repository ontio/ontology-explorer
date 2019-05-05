package com.github.ontio.mapper;

import com.github.ontio.model.Contracts;
import com.github.ontio.model.dao.Contract;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface ContractMapper extends Mapper<Contract> {

    // self-defined SQL

    Contracts selectContractByContractHash(String contractHash);
}