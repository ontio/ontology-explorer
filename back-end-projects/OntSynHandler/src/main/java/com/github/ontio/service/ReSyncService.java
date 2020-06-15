package com.github.ontio.service;

import com.github.ontio.config.ParamsConfig;
import com.github.ontio.mapper.BlockMapper;
import com.github.ontio.mapper.ContractMapper;
import com.github.ontio.mapper.CurrentMapper;
import com.github.ontio.mapper.Oep4TxDetailMapper;
import com.github.ontio.mapper.Oep5DragonMapper;
import com.github.ontio.mapper.Oep5TxDetailMapper;
import com.github.ontio.mapper.Oep8TxDetailMapper;
import com.github.ontio.mapper.OntidTxDetailMapper;
import com.github.ontio.mapper.TxDetailDailyMapper;
import com.github.ontio.mapper.TxDetailIndexMapper;
import com.github.ontio.mapper.TxDetailMapper;
import com.github.ontio.mapper.TxEventLogMapper;
import com.github.ontio.model.common.BatchBlockDto;
import com.github.ontio.model.dao.Oep4TxDetail;
import com.github.ontio.model.dao.Oep5TxDetail;
import com.github.ontio.model.dao.Oep8TxDetail;
import com.github.ontio.model.dao.TxDetail;
import com.github.ontio.model.dao.TxDetailDaily;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/5/8
 */
@Slf4j
@Service
public class ReSyncService {


	private final ParamsConfig paramsConfig;
	private final CurrentMapper currentMapper;
	private final OntidTxDetailMapper ontidTxDetailMapper;
	private final TxDetailMapper txDetailMapper;
	private final Oep4TxDetailMapper oep4TxDetailMapper;
	private final Oep5TxDetailMapper oep5TxDetailMapper;
	private final Oep8TxDetailMapper oep8TxDetailMapper;
	private final TxEventLogMapper txEventLogMapper;
	private final Oep5DragonMapper oep5DragonMapper;
	private final BlockMapper blockMapper;
	private final ContractMapper contractMapper;
	private final TxDetailDailyMapper txDetailDailyMapper;
	private final TxDetailIndexMapper txDetailIndexMapper;

	@Autowired
	public ReSyncService(TxDetailMapper txDetailMapper, ParamsConfig paramsConfig, CurrentMapper currentMapper,
            OntidTxDetailMapper ontidTxDetailMapper,
			Oep4TxDetailMapper oep4TxDetailMapper, Oep5TxDetailMapper oep5TxDetailMapper, Oep8TxDetailMapper oep8TxDetailMapper,
            TxEventLogMapper txEventLogMapper,
			BlockMapper blockMapper, Oep5DragonMapper oep5DragonMapper, ContractMapper contractMapper,
            TxDetailDailyMapper txDetailDailyMapper, TxDetailIndexMapper txDetailIndexMapper) {
		this.txDetailMapper = txDetailMapper;
		this.paramsConfig = paramsConfig;
		this.currentMapper = currentMapper;
		this.ontidTxDetailMapper = ontidTxDetailMapper;
		this.oep4TxDetailMapper = oep4TxDetailMapper;
		this.oep5TxDetailMapper = oep5TxDetailMapper;
		this.oep8TxDetailMapper = oep8TxDetailMapper;
		this.txEventLogMapper = txEventLogMapper;
		this.blockMapper = blockMapper;
		this.oep5DragonMapper = oep5DragonMapper;
		this.contractMapper = contractMapper;
		this.txDetailDailyMapper = txDetailDailyMapper;
		this.txDetailIndexMapper = txDetailIndexMapper;
	}

	/**
	 * 批量插入记录
	 *
	 * @param tHeight
	 * @param batchBlockDto
	 */
	@Transactional(rollbackFor = Exception.class)
	public void batchInsertDb(int beginHeight, int tHeight, BatchBlockDto batchBlockDto, String contractHash) {
		//插入tbl_tx_detail表
		if (batchBlockDto.getTxDetails().size() > 0) {
			int count = batchBlockDto.getTxDetails().size();
			if (count > paramsConfig.BATCHINSERT_SQL_COUNT) {
				for (int j = 0; j <= count / paramsConfig.BATCHINSERT_SQL_COUNT; j++) {
					List<TxDetail> list = batchBlockDto.getTxDetails().subList(j * paramsConfig.BATCHINSERT_SQL_COUNT,
                            (j + 1) * paramsConfig.BATCHINSERT_SQL_COUNT > count ? count :
                                    (j + 1) * paramsConfig.BATCHINSERT_SQL_COUNT);
					if (list.size() > 0) {
						txDetailMapper.batchInsert(list);
					}
				}
			} else {
				txDetailMapper.batchInsert(batchBlockDto.getTxDetails());
			}
			buildTxDetailIndex(beginHeight, tHeight, contractHash);
		}
		//插入tbl_tx_detail_daily表
		if (batchBlockDto.getTxDetailDailys().size() > 0) {
			int count = batchBlockDto.getTxDetailDailys().size();
			if (count > paramsConfig.BATCHINSERT_SQL_COUNT) {
				for (int j = 0; j <= count / paramsConfig.BATCHINSERT_SQL_COUNT; j++) {
					List<TxDetailDaily> list = batchBlockDto.getTxDetailDailys().subList(j * paramsConfig.BATCHINSERT_SQL_COUNT,
                            (j + 1) * paramsConfig.BATCHINSERT_SQL_COUNT > count ? count :
                                    (j + 1) * paramsConfig.BATCHINSERT_SQL_COUNT);
					if (list.size() > 0) {
						txDetailDailyMapper.batchInsert(list);
					}
				}
			} else {
				txDetailDailyMapper.batchInsert(batchBlockDto.getTxDetailDailys());
			}
		}
		//插入tbl_oep4_tx_detail表
		if (batchBlockDto.getOep4TxDetails().size() > 0) {
			int count = batchBlockDto.getOep4TxDetails().size();
			if (count > paramsConfig.BATCHINSERT_SQL_COUNT) {
				for (int j = 0; j <= count / paramsConfig.BATCHINSERT_SQL_COUNT; j++) {
					List<Oep4TxDetail> list = batchBlockDto.getOep4TxDetails().subList(j * paramsConfig.BATCHINSERT_SQL_COUNT,
                            (j + 1) * paramsConfig.BATCHINSERT_SQL_COUNT > count ? count :
                                    (j + 1) * paramsConfig.BATCHINSERT_SQL_COUNT);
					if (list.size() > 0) {
						oep4TxDetailMapper.batchInsert(list);
					}
				}
			} else {
				oep4TxDetailMapper.batchInsert(batchBlockDto.getOep4TxDetails());
			}
		}
		//插入tbl_oep5_tx_detail表
		if (batchBlockDto.getOep5TxDetails().size() > 0) {
			int count = batchBlockDto.getOep5TxDetails().size();
			if (count > paramsConfig.BATCHINSERT_SQL_COUNT) {
				for (int j = 0; j <= count / paramsConfig.BATCHINSERT_SQL_COUNT; j++) {
					List<Oep5TxDetail> list = batchBlockDto.getOep5TxDetails().subList(j * paramsConfig.BATCHINSERT_SQL_COUNT,
                            (j + 1) * paramsConfig.BATCHINSERT_SQL_COUNT > count ? count :
                                    (j + 1) * paramsConfig.BATCHINSERT_SQL_COUNT);
					if (list.size() > 0) {
						oep5TxDetailMapper.batchInsert(list);
					}
				}
			} else {
				oep5TxDetailMapper.batchInsert(batchBlockDto.getOep5TxDetails());
			}
		}
		//插入tbl_oep8_tx_detail表
		if (batchBlockDto.getOep8TxDetails().size() > 0) {
			int count = batchBlockDto.getOep8TxDetails().size();
			if (count > paramsConfig.BATCHINSERT_SQL_COUNT) {
				for (int j = 0; j <= count / paramsConfig.BATCHINSERT_SQL_COUNT; j++) {
					List<Oep8TxDetail> list = batchBlockDto.getOep8TxDetails().subList(j * paramsConfig.BATCHINSERT_SQL_COUNT,
                            (j + 1) * paramsConfig.BATCHINSERT_SQL_COUNT > count ? count :
                                    (j + 1) * paramsConfig.BATCHINSERT_SQL_COUNT);
					if (list.size() > 0) {
						oep8TxDetailMapper.batchInsert(list);
					}
				}
			} else {
				oep8TxDetailMapper.batchInsert(batchBlockDto.getOep8TxDetails());
			}
		}
	}

	private void buildTxDetailIndex(int beginHeight, int endHeight, String contractHash) {
		txDetailIndexMapper.buildTxDetailIndexForFromAddress(beginHeight, endHeight, contractHash);
		txDetailIndexMapper.buildTxDetailIndexForToAddress(beginHeight, endHeight, contractHash);
	}

}
