package com.github.ontio.mapper;

import com.github.ontio.model.dao.NodeRankHistory;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface NodeRankHistoryMapper extends Mapper<NodeRankHistory> {

    Long selectCurrentRoundBlockHeight();

    List<NodeRankHistory> selectRecentNodeRankHistory(Long firstRoundBlockHeight);

}