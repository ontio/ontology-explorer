package com.github.ontio.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.ontio.model.dao.TxEventLog;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Table;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/5/26
 */
@Data
@Table(name = "tbl_tx_eventlog")
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TxEventLogDto extends TxEventLog {
}
