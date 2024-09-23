package com.github.ontio.mapper;

import com.github.ontio.model.dao.BadNode;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


@Repository
public interface BadNodeMapper extends Mapper<BadNode> {

    List<BadNode> selectBadNodeByCycle(int cycle);

}