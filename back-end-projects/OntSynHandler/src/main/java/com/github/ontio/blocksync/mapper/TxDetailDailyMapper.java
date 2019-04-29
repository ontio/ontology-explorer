package com.github.ontio.blocksync.mapper;

import com.github.ontio.blocksync.model.TxDetailDaily;
import com.github.ontio.blocksync.model.TxDetailDailyExample;
import com.github.ontio.blocksync.model.TxDetailDailyKey;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.RowBounds;

public interface TxDetailDailyMapper {
    long countByExample(TxDetailDailyExample example);

    int deleteByExample(TxDetailDailyExample example);

    @Delete({
        "delete from tbl_tx_detail_daily",
        "where tx_hash = #{txHash,jdbcType=VARCHAR}",
          "and tx_index = #{txIndex,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(TxDetailDailyKey key);

    @Insert({
        "insert into tbl_tx_detail_daily (tx_hash, tx_index, ",
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
    int insert(TxDetailDaily record);

    int insertSelective(TxDetailDaily record);

    List<TxDetailDaily> selectByExampleWithRowbounds(TxDetailDailyExample example, RowBounds rowBounds);

    List<TxDetailDaily> selectByExample(TxDetailDailyExample example);

    @Select({
        "select",
        "tx_hash, tx_index, tx_type, tx_time, block_height, amount, fee, asset_name, ",
        "from_address, to_address, description, block_index, confirm_flag, event_type, ",
        "contract_hash, payer, called_contract_hash",
        "from tbl_tx_detail_daily",
        "where tx_hash = #{txHash,jdbcType=VARCHAR}",
          "and tx_index = #{txIndex,jdbcType=INTEGER}"
    })
    @ResultMap("com.github.ontio.blocksync.mapper.TxDetailDailyMapper.BaseResultMap")
    TxDetailDaily selectByPrimaryKey(TxDetailDailyKey key);

    int updateByExampleSelective(@Param("record") TxDetailDaily record, @Param("example") TxDetailDailyExample example);

    int updateByExample(@Param("record") TxDetailDaily record, @Param("example") TxDetailDailyExample example);

    int updateByPrimaryKeySelective(TxDetailDaily record);

    @Update({
        "update tbl_tx_detail_daily",
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
    int updateByPrimaryKey(TxDetailDaily record);

    // Self-defined mapper

    @Select({
            "select count(*)",
            "from tbl_tx_detail_daily",
            "where height = #{height,jdbcType=INTEGER}"
    })
    int selectCountByHeight(Integer height);

    @Delete({
            "delete from tbl_tx_detail_daily",
            "where height = #{height,jdbcType=INTEGER}"
    })
    int deleteByHeight(Integer height);
}