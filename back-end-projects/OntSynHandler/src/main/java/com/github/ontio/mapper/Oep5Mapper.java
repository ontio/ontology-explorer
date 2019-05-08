package com.github.ontio.mapper;

import com.github.ontio.model.dao.Oep5;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
@Component
public interface Oep5Mapper extends Mapper<Oep5> {

    List<Oep5> selectApprovedRecords();


}