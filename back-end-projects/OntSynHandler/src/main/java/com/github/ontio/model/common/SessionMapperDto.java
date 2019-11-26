package com.github.ontio.model.common;

import com.github.ontio.mapper.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/4/30
 */
@NoArgsConstructor
@Data
public class SessionMapperDto {

    private TxDetailMapper txDetailMapper;

    private TxDetailDailyMapper txDetailDailyMapper;

    private Oep4TxDetailMapper oep4TxDetailMapper;

    private Oep5TxDetailMapper oep5TxDetailMapper;

    private Oep8TxDetailMapper oep8TxDetailMapper;

    private OntidTxDetailMapper ontidTxDetailMapper;

    private ContractMapper contractMapper;

    private TxEventLogMapper txEventLogMapper;

    private Oep5DragonMapper oep5DragonMapper;

    private Oep5Mapper oep5Mapper;

    private Oep8Mapper oep8Mapper;


    @Builder
    public SessionMapperDto(TxDetailMapper txDetailMapper, TxDetailDailyMapper txDetailDailyMapper, Oep4TxDetailMapper oep4TxDetailMapper, Oep5TxDetailMapper oep5TxDetailMapper, Oep8TxDetailMapper oep8TxDetailMapper, OntidTxDetailMapper ontidTxDetailMapper, ContractMapper contractMapper, TxEventLogMapper txEventLogMapper, Oep5DragonMapper oep5DragonMapper, Oep5Mapper oep5Mapper, Oep8Mapper oep8Mapper) {
        this.txDetailMapper = txDetailMapper;
        this.txDetailDailyMapper = txDetailDailyMapper;
        this.oep4TxDetailMapper = oep4TxDetailMapper;
        this.oep5TxDetailMapper = oep5TxDetailMapper;
        this.oep8TxDetailMapper = oep8TxDetailMapper;
        this.ontidTxDetailMapper = ontidTxDetailMapper;
        this.contractMapper = contractMapper;
        this.txEventLogMapper = txEventLogMapper;
        this.oep5DragonMapper = oep5DragonMapper;
        this.oep5Mapper = oep5Mapper;
        this.oep8Mapper = oep8Mapper;
    }
}
