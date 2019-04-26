package com.github.ontio.config;

import com.github.ontio.exception.ExplorerException;
import com.github.ontio.paramBean.ResponseBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 异常控制处理器
 */
@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

    /**
     * 捕捉校验异常(BindException)
     *
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ResponseBean validException(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        Map<String, Object> result = this.getValidError(fieldErrors);
        return new ResponseBean(HttpStatus.BAD_REQUEST.value(), result.get("errorMsg").toString(), result.get("errorList"));
    }

    /**
     * 捕捉校验异常(MethodArgumentNotValidException)
     *
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseBean validException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        Map<String, Object> result = this.getValidError(fieldErrors);
        return new ResponseBean(HttpStatus.BAD_REQUEST.value(), result.get("errorMsg").toString(), result.get("errorList"));
    }

    /**
     * 捕捉自定义业务类异常
     *
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ExplorerException.class)
    public ResponseBean handle(ExplorerException e) {

        return new ResponseBean(e.getCode(), e.getMsg(), e.getResult());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseBean handle(ConstraintViolationException e, HttpServletRequest httpServletRequest) {
        return new ResponseBean(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
    }

    /**
     * 捕捉404异常
     *
     * @return
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseBean handle(NoHandlerFoundException e) {
        return new ResponseBean(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
    }

    /**
     * 捕捉其他所有异常
     *
     * @param request
     * @param ex
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseBean globalException(HttpServletRequest request, Throwable ex) {
        log.error("error...", ex);
        return new ResponseBean(this.getStatus(request).value(), ex.getMessage(), null);
    }

    /**
     * 获取状态码
     *
     * @param request
     * @return
     */
    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }

    /**
     * 获取校验错误信息
     *
     * @param fieldErrors
     * @return
     */
    private Map<String, Object> getValidError(List<FieldError> fieldErrors) {
        Map<String, Object> result = new HashMap<String, Object>(16);
        List<String> errorList = new ArrayList<String>();
        StringBuffer errorMsg = new StringBuffer("ValidException:");
        for (FieldError error : fieldErrors) {
            errorList.add(error.getField() + "-" + error.getDefaultMessage());
            errorMsg.append(error.getField() + "-" + error.getDefaultMessage() + ".");
        }
        result.put("errorList", errorList);
        result.put("errorMsg", errorMsg);
        return result;
    }
}
