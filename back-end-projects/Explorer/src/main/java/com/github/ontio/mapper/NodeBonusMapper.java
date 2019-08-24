package com.github.ontio.mapper;

import com.github.ontio.model.dao.NodeBonus;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface NodeBonusMapper extends Mapper<NodeBonus> {
    Integer selectNodeCount();

    List<NodeBonus> selectLatestNodeBonusList(@Param("nodeCount") Integer nodeCount);

    NodeBonus selectLatestNodeBonusByPublicKey(@Param("publicKey") String publicKey);

    NodeBonus selectLatestNodeBonusByAddress(@Param("address") String address);

    List<NodeBonus> searchLatestNodeBonusListByName(@Param("name") String name);

}