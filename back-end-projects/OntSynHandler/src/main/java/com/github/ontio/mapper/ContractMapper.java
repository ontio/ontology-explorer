package com.github.ontio.mapper;

import com.github.ontio.model.dao.Contract;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;
@Component
public interface ContractMapper extends Mapper<Contract> {
}