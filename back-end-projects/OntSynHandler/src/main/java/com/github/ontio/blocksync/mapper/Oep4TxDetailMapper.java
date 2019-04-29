package com.github.ontio.blocksync.mapper;

import com.github.ontio.blocksync.model.Oep4TxDetail;
import com.github.ontio.blocksync.model.Oep4TxDetailExample;
import com.github.ontio.blocksync.model.Oep4TxDetailKey;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.RowBounds;

public interface Oep4TxDetailMapper {
    long countByExample(Oep4TxDetailExample example);

    int deleteByExample(Oep4TxDetailExample example);

    @Delete({
        "delete from tbl_oep4_tx_detail",
        "where tx_hash = #{txHash,jdbcType=VARCHAR}",
          "and tx_index = #{txIndex,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Oep4TxDetailKey key);

    @Insert({
        "insert into tbl_oep4_tx_detail (tx_hash, tx_index, ",
        "tx_type, tx_time, ",
        "block_height, amount, ",
        "fee, asset_name, ",
        "from_address, to_address, ",
        "description, block_index, ",
        "confirm_flag, event_type, ",
        "contract_hash, payer, ",
        "called_contract_hash)",
        "values (#{txHash,jdbcType=VARCHAR}, #{txIndex,jdbcType=INTEGER}, ",
        "#{txType,jdbcType=INTEGER}, #{txTime,jdbcType=INTEGER}, ",
        "#{blockHeight,jdbcType=INTEGER}, #{amount,jdbcType=DECIMAL}, ",
        "#{fee,jdbcType=DECIMAL}, #{assetName,jdbcType=VARCHAR}, ",
        "#{fromAddress,jdbcType=VARCHAR}, #{toAddress,jdbcType=VARCHAR}, ",
        "#{description,jdbcType=VARCHAR}, #{blockIndex,jdbcType=INTEGER}, ",
        "#{confirmFlag,jdbcType=INTEGER}, #{eventType,jdbcType=INTEGER}, ",
        "#{contractHash,jdbcType=VARCHAR}, #{payer,jdbcType=VARCHAR}, ",
        "#{calledContractHash,jdbcType=VARCHAR})"
    })
    int insert(Oep4TxDetail record);

    int insertSelective(Oep4TxDetail record);

    List<Oep4TxDetail> selectByExampleWithRowbounds(Oep4TxDetailExample example, RowBounds rowBounds);

    List<Oep4TxDetail> selectByExample(Oep4TxDetailExample example);

    @Select({
        "select",
        "tx_hash, tx_index, tx_type, tx_time, block_height, amount, fee, asset_name, ",
        "from_address, to_address, description, block_index, confirm_flag, event_type, ",
        "contract_hash, payer, called_contract_hash",
        "from tbl_oep4_tx_detail",
        "where tx_hash = #{txHash,jdbcType=VARCHAR}",
          "and tx_index = #{txIndex,jdbcType=INTEGER}"
    })
    @ResultMap("com.github.ontio.blocksync.mapper.Oep4TxDetailMapper.BaseResultMap")
    Oep4TxDetail selectByPrimaryKey(Oep4TxDetailKey key);

    int updateByExampleSelective(@Param("record") Oep4TxDetail record, @Param("example") Oep4TxDetailExample example);

    int updateByExample(@Param("record") Oep4TxDetail record, @Param("example") Oep4TxDetailExample example);

    int updateByPrimaryKeySelective(Oep4TxDetail record);

    @Update({
        "update tbl_oep4_tx_detail",
        "set tx_type = #{txType,jdbcType=INTEGER},",
          "tx_time = #{txTime,jdbcType=INTEGER},",
          "block_height = #{blockHeight,jdbcType=INTEGER},",
          "amount = #{amount,jdbcType=DECIMAL},",
          "fee = #{fee,jdbcType=DECIMAL},",
          "asset_name = #{assetName,jdbcType=VARCHAR},",
          "from_address = #{fromAddress,jdbcType=VARCHAR},",
          "to_address = #{toAddress,jdbcType=VARCHAR},",
          "description = #{description,jdbcType=VARCHAR},",
          "block_index = #{blockIndex,jdbcType=INTEGER},",
          "confirm_flag = #{confirmFlag,jdbcType=INTEGER},",
          "event_type = #{eventType,jdbcType=INTEGER},",
          "contract_hash = #{contractHash,jdbcType=VARCHAR},",
          "payer = #{payer,jdbcType=VARCHAR},",
          "called_contract_hash = #{calledContractHash,jdbcType=VARCHAR}",
        "where tx_hash = #{txHash,jdbcType=VARCHAR}",
          "and tx_index = #{txIndex,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Oep4TxDetail record);
}