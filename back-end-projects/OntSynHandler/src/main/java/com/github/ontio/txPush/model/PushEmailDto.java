package com.github.ontio.txPush.model;

import com.github.ontio.model.dao.TxDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zhouq
 * @version 1.0
 * @date 2020/3/27
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PushEmailDto {

    private String email;
    private String userName;
    private String ontId;
    private String userAddress;
    private String txDes;
    private String txHash;
    private String assetName;
    private String amount;
    private String time;
    private String tAddress;

    public static final String DEPOSIT = "deposit";
    public static final String WITHDRAW = "withdraw";

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static PushEmailDto buildDto(PushUserAddressInfoDto pushUserAddressInfoDto, TxDetail txDetail, String txDes) {
        PushEmailDtoBuilder builder = PushEmailDto.builder()
                .email(pushUserAddressInfoDto.getEmail())
                .userName(pushUserAddressInfoDto.getUserName())
                .ontId(pushUserAddressInfoDto.getOntId())
                .txHash(txDetail.getTxHash())
                .amount(txDetail.getAmount().stripTrailingZeros().toPlainString())
                .time(sdf.format(new Date(txDetail.getTxTime() * 1000L)))
                .assetName(txDetail.getAssetName());
        if (DEPOSIT.equals(txDes)) {
            return builder.userAddress(txDetail.getToAddress())
                    .tAddress(txDetail.getFromAddress())
                    .txDes(DEPOSIT)
                    .build();
        } else {
            return builder.userAddress(txDetail.getFromAddress())
                    .tAddress(txDetail.getToAddress())
                    .txDes(WITHDRAW)
                    .build();
        }
    }


}
