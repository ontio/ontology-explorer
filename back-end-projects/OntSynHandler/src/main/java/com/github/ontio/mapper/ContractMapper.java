package com.github.ontio.mapper;

import com.github.ontio.model.dao.Contract;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Component
public interface ContractMapper extends Mapper<Contract> {

    void batchInsert(List<Contract> list);

}