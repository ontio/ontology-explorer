package com.github.ontio.model.dto.login;

import com.github.ontio.util.JacksonUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhouq
 * @version 1.0
 * @date 2020/3/25
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QrCodeDto {

    private String callback;
    private String chain;
    private String data;
    private long exp;
    private String id;
    private String requester;
    private String signature;
    private String signer;
    private String ver;


    public static QrCodeDto mainNetLoginQrCode(String id, String requester, String signature, QrCodeData qrCodeData, String callback, long exp) {
        QrCodeDto qrCodeDto = QrCodeDto.builder()
                .ver("1.0.0")
                .id(id)
                .requester(requester)
                .signature(signature)
                .signer("")
                .data(JacksonUtil.beanToJSonStr(qrCodeData))
                .callback(callback)
                .exp(exp)
                .chain("Mainnet")
                .build();
        return qrCodeDto;
    }

    public static QrCodeDto testNetLoginQrCode(String id, String requester, String signature, QrCodeData qrCodeData, String callback, long exp) {
        QrCodeDto qrCodeDto = QrCodeDto.builder()
                .ver("1.0.0")
                .id(id)
                .requester(requester)
                .signature(signature)
                .signer("")
                .data(JacksonUtil.beanToJSonStr(qrCodeData))
                .callback(callback)
                .exp(exp)
                .chain("Testnet")
                .build();
        return qrCodeDto;
    }


    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    public static class QrCodeData {
        private String action;
        private QrCodeDataParam params;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    public static class QrCodeDataParam {
        private QrCodeDataParamDetail invokeConfig;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    public static class QrCodeDataParamDetail {
        private String contractHash;
        private List<QrCodeDataParamFun> functions;
        private BigDecimal gasLimit;
        private BigDecimal gasPrice;
        private String payer;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    public static class QrCodeDataParamFun {
        private List<QrCodeDataParamFunArg> args;
        private String operation;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    public static class QrCodeDataParamFunArg {
        private String name;
        private String value;
    }

}
