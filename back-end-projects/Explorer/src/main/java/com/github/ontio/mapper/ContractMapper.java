package com.github.ontio.mapper;

import com.github.ontio.model.dto.ContractDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@Repository
public interface ContractMapper extends Mapper<ContractDto> {

    List<ContractDto> selectApprovedContract(@Param("startIndex") int startIndex, @Param("pageSize") int pageSize);

    Integer selectApprovedContractCount();

    ContractDto selectContractDetail(@Param("contractHash") String contractHash);

    List<Map<String,String>> selectContractHashByDappName(@Param("dappNameList") List<String> dappNameList);
}