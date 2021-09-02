package com.github.ontio.mapper;

import com.github.ontio.model.dao.Orc20;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Component
public interface Orc20Mapper extends Mapper<Orc20> {

    List<Orc20> selectApprovedRecords();
}
