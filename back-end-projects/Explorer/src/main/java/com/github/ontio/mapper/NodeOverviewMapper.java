package com.github.ontio.mapper;

import com.github.ontio.model.dao.NodeOverview;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface NodeOverviewMapper extends Mapper<NodeOverview> {

    Long selectBlkCountToNxtRnd();

    Long selectLeftTimeToNxtRnd();

}