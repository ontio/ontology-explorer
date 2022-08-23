package com.github.ontio.model.common;

import com.github.ontio.model.dao.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/5/17
 */
@Data
public class BatchBlockDto {
    //list会放到子线程进行操作，必须初始化成线程安全的list
    private List<Block> blocks = Collections.synchronizedList(new ArrayList<Block>());

    private List<Contract> contracts = Collections.synchronizedList(new ArrayList<Contract>());

    private List<TxDetail> txDetails = Collections.synchronizedList(new ArrayList<TxDetail>());

    private List<TxDetailDaily> txDetailDailys = Collections.synchronizedList(new ArrayList<TxDetailDaily>());

    private List<TxEventLog> txEventLogs = Collections.synchronizedList(new ArrayList<TxEventLog>());

    private List<OntidTxDetail> ontidTxDetails = Collections.synchronizedList(new ArrayList<OntidTxDetail>());

    private List<Oep4TxDetail> oep4TxDetails = Collections.synchronizedList(new ArrayList<Oep4TxDetail>());

    private List<Oep5TxDetail> oep5TxDetails = Collections.synchronizedList(new ArrayList<Oep5TxDetail>());

    private List<Oep5Dragon> oep5Dragons = Collections.synchronizedList(new ArrayList<Oep5Dragon>());

    private List<Oep8TxDetail> oep8TxDetails = Collections.synchronizedList(new ArrayList<Oep8TxDetail>());

    private List<Orc20TxDetail> orc20TxDetails = Collections.synchronizedList(new ArrayList<Orc20TxDetail>());

    private List<Orc721TxDetail> orc721TxDetails = Collections.synchronizedList(new ArrayList<Orc721TxDetail>());

    private List<Orc1155TxDetail> orc1155TxDetails = Collections.synchronizedList(new ArrayList<Orc1155TxDetail>());

    private List<NodeAuthorizeInfo> stakeNodeDetails = Collections.synchronizedList(new ArrayList<>());

}
