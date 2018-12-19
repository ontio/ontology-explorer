package com.github.ontio.dao;

import com.github.ontio.model.ContractSummary;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component(value = "ContractSummaryMapper")
public interface ContractSummaryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ContractSummary record);

    int insertSelective(ContractSummary record);

    int banchInsertSelective(List<ContractSummary> record);

    ContractSummary selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ContractSummary record);

    int updateByPrimaryKey(ContractSummary record);

    List<Map> selectDailySummaryByContractHash(Map<String, Object> paramMap);
}