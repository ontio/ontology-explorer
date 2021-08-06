package com.github.ontio.mapper;

import com.github.ontio.model.dao.Erc20;
import com.github.ontio.model.dao.Erc721;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Component
public interface Erc721Mapper extends Mapper<Erc721> {

    List<Erc721> selectApprovedRecords();
}
