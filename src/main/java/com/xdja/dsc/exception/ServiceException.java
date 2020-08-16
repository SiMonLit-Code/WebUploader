package com.xdja.dsc.exception;

import com.xdja.dsc.enums.ExceptionEnum;
import lombok.Getter;


/**
 * 全部异常异常处理类
 *
 * @author Created by niugang on 2018/12/26/12:20
 */
@Getter
public class ServiceException extends RuntimeException {

    private ExceptionEnum exceptionEnum;

    public ServiceException(ExceptionEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
    }

}
