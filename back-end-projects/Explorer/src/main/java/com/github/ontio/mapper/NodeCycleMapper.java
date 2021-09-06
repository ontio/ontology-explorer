package com.github.ontio.mapper;

import com.github.ontio.model.dao.NodeCycle;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import java.util.List;

@Repository
public interface NodeCycleMapper extends Mapper<NodeCycle> {
    List<NodeCycle> selectCycleByPubKey(String publicKey);

    List<NodeCycle> selectAllCycleData();

}