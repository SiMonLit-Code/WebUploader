package com.xdja.dsc.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author Created by niugang on 2018/12/26/12:18
 */
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnum {
    /**
     *
     */
    PARAMS_VALIDATE_FAIL(400, "参数校验失败"),
    BUSINESS_DEAL_FAIL(500, "'业务处理失败"),
    FILE_NOT_EXIST(400, "文件不存在"),
    DECRYPT_FAILED(500, "解密失败"),
    DOWNLOAD_FAILED(500, "下载失败");
    /**
     * 响应状态码
     */
    int value;
    /**
     * 响应描述
     */
    String message;

    public int value() {
        return this.value;
    }

    public String message() {
        return this.message;
    }
}
