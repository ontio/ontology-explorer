package com.github.ontio.model.common;

import lombok.Data;

/**
 * @author zhouq
 * @version 1.0
 * @date 2019/4/26
 */
@Data
public class ResponseBean {

    private Integer code;

    private String msg;

    private Object result;

    public ResponseBean(Integer code, String msg, Object result){
        this.code = code;
        this.msg = msg;
        this.result = result;
    }

    public ResponseBean(){}


}
