package com.github.ontio.mapper;

import com.github.ontio.model.dao.NodeCycle;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import java.util.List;

@Repository
public interface NodeCycleMapper extends Mapper<NodeCycle> {
    List<NodeCycle> selectCycleByPubKey(@Param("publicKey") String publicKey, @Param("start") int start, @Param("size") int size);

    List<NodeCycle> selectAllCycleData(@Param("start") int start, @Param("size") int size);

    int selectNodeCycleCount();

    int selectNodeCycleCountByPublicKey(@Param("publicKey") String publicKey);


}