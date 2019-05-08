package com.github.ontio.mapper;

import com.github.ontio.model.dao.Oep5;
import com.github.ontio.model.dto.Oep5DetailDto;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface Oep5Mapper extends Mapper<Oep5> {

    List<Oep5DetailDto> selectOep5Tokens();

}