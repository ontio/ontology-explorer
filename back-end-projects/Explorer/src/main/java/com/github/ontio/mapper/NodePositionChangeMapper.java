package com.github.ontio.mapper;

import com.github.ontio.model.dao.NodePositionChange;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface NodePositionChangeMapper extends Mapper<NodePositionChange> {

    List<NodePositionChange> selectAllChangeInfoInAsc();

    List<NodePositionChange> selectAllChangeInfoInDesc();
}