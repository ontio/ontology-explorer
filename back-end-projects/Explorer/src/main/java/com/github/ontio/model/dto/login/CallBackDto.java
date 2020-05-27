package com.github.ontio.model.dto.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhouq
 * @version 1.0
 * @date 2020/3/25
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CallBackDto {

    private String signer;
    private String signedTx;
    private CallbackExtraData extraData;

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Data
    public static class CallbackExtraData{
        private String id;
    }

}
