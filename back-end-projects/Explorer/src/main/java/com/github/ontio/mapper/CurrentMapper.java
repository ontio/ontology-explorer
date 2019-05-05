package com.github.ontio.mapper;

import com.github.ontio.model.dto.CurrentDto;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.Map;

@Repository
public interface CurrentMapper extends Mapper<CurrentDto> {

    // self-defined SQL

    Map selectSummaryInfo();

}