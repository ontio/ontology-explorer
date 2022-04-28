package com.github.ontio.mapper;

import com.github.ontio.model.dao.ContractCompileInfo;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface ContractCompileInfoMapper extends Mapper<ContractCompileInfo> {
}