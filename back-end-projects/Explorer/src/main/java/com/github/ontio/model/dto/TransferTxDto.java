package com.github.ontio.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.ontio.model.dao.TxDetail;
import lombok.Data;

import java.util.List;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/5/9
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class TransferTxDto extends TxDetail{

    List<TransferTxDetailDto> transfers;

}
