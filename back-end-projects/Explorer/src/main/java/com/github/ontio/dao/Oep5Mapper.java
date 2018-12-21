package com.github.ontio.dao;

import com.github.ontio.model.Oep4;
import com.github.ontio.model.Oep5;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface Oep5Mapper {
    int deleteByPrimaryKey(String contract);

    int insert(Oep5 record);

    int insertSelective(Oep5 record);

    Oep5 selectByPrimaryKey(String contract);

    int updateByPrimaryKeySelective(Oep5 record);

    int updateByPrimaryKey(Oep5 record);

    List<Map> queryOEPContracts(Map<String, Object> param);

    Integer queryOEPContractCount();

    Oep5 queryOEPContract(String contractHash);

    int deletContractByHash(String contractHash);

    List<Map> selectAllKeyInfo();
}