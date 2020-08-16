package com.xdja.dsc.vo;


import com.xdja.dsc.enums.ExceptionEnum;
import lombok.Data;


/**
 * 异常响应封装
 *
 * @author Created by niugang on 2018/12/26/12:20
 */
@Data
public class ExceptionResult {

    /**
     * 状态码
     */
    private int status;
    /**
     * 描述
     */
    private String message;

    /**
     * 时间戳
     */
    private long timestamp;

    public ExceptionResult(ExceptionEnum em) {
        this.status = em.value();
        this.message = em.message();
        this.timestamp = System.currentTimeMillis();
    }
}
