package com.github.ontio.exception;

import com.github.ontio.util.ErrorInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ExplorerException extends RuntimeException {

    private Integer code;

    private String msg;

    private Object result;

    public ExplorerException(Integer code, String msg, Object result){
        this.code = code;
        this.msg = msg;
        this.result = result;
    }

    public ExplorerException(ErrorInfo errorInfo, Object result){
        this.code = errorInfo.code();
        this.msg = errorInfo.desc();
        this.result = result;
    }

    public ExplorerException(ErrorInfo errorInfo){
        this.code = errorInfo.code();
        this.msg = errorInfo.desc();
        this.result = false;
    }

    public ExplorerException(){super();}

    public ExplorerException(String msg){super(msg);}


}
