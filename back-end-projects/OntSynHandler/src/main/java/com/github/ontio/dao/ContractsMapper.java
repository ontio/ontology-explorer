package com.github.ontio.dao;

import com.github.ontio.model.Contracts;
import org.springframework.stereotype.Component;
import java.util.List;


@Component
public interface ContractsMapper {
    int deleteByPrimaryKey(String contract);

    int insert(Contracts record);

    int insertSelective(Contracts record);

    Contracts selectByPrimaryKey(String contract);

    int updateByPrimaryKeySelective(Contracts record);

    int updateByPrimaryKey(Contracts record);

    boolean updateContractTxCount(List<Contracts> contractsList);
}