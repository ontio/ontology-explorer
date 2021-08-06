package com.github.ontio.mapper;

import com.github.ontio.model.dao.Erc20;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Component
public interface Erc20Mapper extends Mapper<Erc20> {

    List<Erc20> selectApprovedRecords();
}
