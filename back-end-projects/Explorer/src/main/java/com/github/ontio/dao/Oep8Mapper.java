package com.github.ontio.dao;

import com.github.ontio.model.Oep8;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface Oep8Mapper {
    int deleteByPrimaryKey(Long id);

    int insert(Oep8 record);

    int insertSelective(Oep8 record);

    int banchInsertSelective(List<Oep8> list);

    Oep8 selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Oep8 record);

    int updateByPrimaryKey(Oep8 record);

    List<Map> queryOEPContracts(Map<String, Object> param);

    Integer queryOEPContractCount();

    Oep8 queryOEPContract(String contractHash);

    Oep8 queryOEPContractByHashAndTokenName(String contractHash, String tokenName);

    int deletContractByHash(String contractHash);

    List<Map> selectAllKeyInfo();
}