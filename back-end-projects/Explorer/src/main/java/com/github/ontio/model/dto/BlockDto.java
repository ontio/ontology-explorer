package com.github.ontio.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.ontio.model.dao.Block;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Table;
import java.util.List;

@Data
@Table(name = "tbl_block")
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlockDto extends Block {

    private List<TxBasicDto> txs;

    @Builder
    public BlockDto(Integer blockHeight, String blockHash, String txsRoot, Integer blockTime, String consensusData, String bookkeepers, Integer txCount, Integer blockSize, List<TxBasicDto> txs) {
        super(blockHeight, blockHash, txsRoot, blockTime, consensusData, bookkeepers, txCount, blockSize);
        this.txs = txs;
    }
}
