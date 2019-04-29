package com.github.ontio.blocksync.mapper;

import com.github.ontio.blocksync.model.NodeInfo;
import com.github.ontio.blocksync.model.NodeInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.RowBounds;

public interface NodeInfoMapper {
    long countByExample(NodeInfoExample example);

    int deleteByExample(NodeInfoExample example);

    @Delete({
        "delete from t_node_info",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into t_node_info (id, name, ",
        "node_rank, current_stake, ",
        "progress, detail_url, ",
        "node_index, public_key, ",
        "address, status, ",
        "init_pos, total_pos, ",
        "max_authorize, node_proportion)",
        "values (#{id,jdbcType=BIGINT}, #{name,jdbcType=VARCHAR}, ",
        "#{nodeRank,jdbcType=INTEGER}, #{currentStake,jdbcType=BIGINT}, ",
        "#{progress,jdbcType=VARCHAR}, #{detailUrl,jdbcType=VARCHAR}, ",
        "#{nodeIndex,jdbcType=INTEGER}, #{publicKey,jdbcType=VARCHAR}, ",
        "#{address,jdbcType=VARCHAR}, #{status,jdbcType=INTEGER}, ",
        "#{initPos,jdbcType=BIGINT}, #{totalPos,jdbcType=BIGINT}, ",
        "#{maxAuthorize,jdbcType=BIGINT}, #{nodeProportion,jdbcType=VARCHAR})"
    })
    int insert(NodeInfo record);

    int insertSelective(NodeInfo record);

    List<NodeInfo> selectByExampleWithRowbounds(NodeInfoExample example, RowBounds rowBounds);

    List<NodeInfo> selectByExample(NodeInfoExample example);

    @Select({
        "select",
        "id, name, node_rank, current_stake, progress, detail_url, node_index, public_key, ",
        "address, status, init_pos, total_pos, max_authorize, node_proportion",
        "from t_node_info",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @ResultMap("com.github.ontio.blocksync.mapper.NodeInfoMapper.BaseResultMap")
    NodeInfo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") NodeInfo record, @Param("example") NodeInfoExample example);

    int updateByExample(@Param("record") NodeInfo record, @Param("example") NodeInfoExample example);

    int updateByPrimaryKeySelective(NodeInfo record);

    @Update({
        "update t_node_info",
        "set name = #{name,jdbcType=VARCHAR},",
          "node_rank = #{nodeRank,jdbcType=INTEGER},",
          "current_stake = #{currentStake,jdbcType=BIGINT},",
          "progress = #{progress,jdbcType=VARCHAR},",
          "detail_url = #{detailUrl,jdbcType=VARCHAR},",
          "node_index = #{nodeIndex,jdbcType=INTEGER},",
          "public_key = #{publicKey,jdbcType=VARCHAR},",
          "address = #{address,jdbcType=VARCHAR},",
          "status = #{status,jdbcType=INTEGER},",
          "init_pos = #{initPos,jdbcType=BIGINT},",
          "total_pos = #{totalPos,jdbcType=BIGINT},",
          "max_authorize = #{maxAuthorize,jdbcType=BIGINT},",
          "node_proportion = #{nodeProportion,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(NodeInfo record);
}