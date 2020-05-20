package com.github.ontio.txPush.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.ontio.model.dao.TxDetail;
import com.github.ontio.utils.ConstantParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author zhouq
 * @version 1.0
 * @date 2020/3/27
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PushEmailDto {

    private String email;
    private String userName;
    private String ontId;
    private String userAddress;
    private String note;
    private String txDes;
    private String txHash;
    private String assetName;
    private String amount;
    private long time;
    private String toAddress;
    private String fromAddress;
    private String channel;
    private BigDecimal amountThreshold;

    public static final String DEPOSIT = "deposit";
    public static final String WITHDRAW = "withdraw";


    public static PushEmailDto buildDto(PushUserAddressInfoDto pushUserAddressInfoDto, TxDetail txDetail, String txDes) {
        PushEmailDtoBuilder builder = PushEmailDto.builder()
                .email(pushUserAddressInfoDto.getEmail())
                .userName(pushUserAddressInfoDto.getUserName())
                .ontId(pushUserAddressInfoDto.getOntId())
                .note(pushUserAddressInfoDto.getNote())
                .channel(pushUserAddressInfoDto.getChannel())
                .amountThreshold(pushUserAddressInfoDto.getAmountThreshold())
                .txHash(txDetail.getTxHash())
                .time(txDetail.getTxTime())
                .assetName(txDetail.getAssetName())
                .fromAddress(txDetail.getFromAddress())
                .toAddress(txDetail.getToAddress());
        if (ConstantParam.ASSET_NAME_ONG.equals(txDetail.getAssetName())) {
            builder.amount(txDetail.getAmount().divide(ConstantParam.ONG_DECIMAL).stripTrailingZeros().toPlainString());
        } else {
            builder.amount(txDetail.getAmount().stripTrailingZeros().toPlainString());
        }
        if (DEPOSIT.equals(txDes)) {
            return builder.userAddress(txDetail.getToAddress())
                    .txDes(DEPOSIT)
                    .build();
        } else {
            return builder.userAddress(txDetail.getFromAddress())
                    .txDes(WITHDRAW)
                    .build();
        }
    }


}
