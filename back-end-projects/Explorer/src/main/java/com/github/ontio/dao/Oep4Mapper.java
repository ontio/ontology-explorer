package com.github.ontio.dao;

import com.github.ontio.model.Oep4;

import java.util.List;
import java.util.Map;

public interface Oep4Mapper {
    int insert(Oep4 record);

    int insertSelective(Oep4 record);

    Oep4 selectByPrimaryKey(Oep4 key);

    int updateByPrimaryKeySelective(Oep4 record);

    int updateByPrimaryKey(Oep4 record);

    List<Map> selectAllKeyInfo();

    List<Map> queryOEPContracts(Map<String, Object> param);

    Oep4 queryOEPContract(String contractHash);

    Integer queryOEPContractCount();
}