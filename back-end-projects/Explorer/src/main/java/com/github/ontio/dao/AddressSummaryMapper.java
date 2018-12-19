package com.github.ontio.dao;

import com.github.ontio.model.AddressSummary;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "AddressSummaryMapper")
public interface AddressSummaryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AddressSummary record);

    int banchInsertSelective(List<AddressSummary> records);

    AddressSummary selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AddressSummary record);

    int updateByPrimaryKey(AddressSummary record);

    List<String> selectDistinctAddress();

    List<String> selectDistinctAddressByContract(String contractHash);

    Integer selectAllAddressCount();
}