package com.github.ontio.mapper;

import com.github.ontio.model.dao.Orc1155;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Component
public interface Orc1155Mapper extends Mapper<Orc1155> {

    List<Orc1155> selectApprovedRecords();

}