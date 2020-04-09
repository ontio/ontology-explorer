package com.github.ontio.model.dto.login;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author zhouq
 * @version 1.0
 * @date 2020/3/25
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CallBackResponse {
    private String action;
    private String version;
    private long error;
    private String id;
    private JSONObject result;


    public static CallBackResponse successResponse(JSONObject object) {
        CallBackResponse callBackResponse = CallBackResponse.builder()
                .action("")
                .version("1.0.0")
                .id(UUID.randomUUID().toString())
                .error(0L)
                .result(object)
                .build();
        return callBackResponse;
    }

    public static CallBackResponse errorResponse(long error) {
        CallBackResponse callBackResponse = CallBackResponse.builder()
                .action("")
                .version("1.0.0")
                .id(UUID.randomUUID().toString())
                .error(error)
                .result(new JSONObject())
                .build();
        return callBackResponse;
    }

}
