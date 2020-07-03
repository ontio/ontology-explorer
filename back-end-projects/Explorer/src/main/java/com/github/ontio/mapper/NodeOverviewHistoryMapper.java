package com.github.ontio.mapper;

import com.github.ontio.model.dao.NodeOverviewHistory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface NodeOverviewHistoryMapper extends Mapper<NodeOverviewHistory> {

    List<NodeOverviewHistory> selectNodeRndHistory(@Param("start") int start, @Param("size") int size);

    int selectNodeRndHistoryCount();

}