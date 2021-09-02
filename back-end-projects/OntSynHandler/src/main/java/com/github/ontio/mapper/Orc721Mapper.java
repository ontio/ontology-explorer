package com.github.ontio.mapper;

import com.github.ontio.model.dao.Orc721;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


@Component
public interface Orc721Mapper extends Mapper<Orc721> {
    List<Orc721> selectApprovedRecords();
}
