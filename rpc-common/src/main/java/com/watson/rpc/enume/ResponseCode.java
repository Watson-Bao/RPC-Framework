package com.watson.rpc.enume;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author watson
 */
@AllArgsConstructor
@Getter
public enum ResponseCode {
    /**
     * 响应状态码
     */
    SUCCESS(200, "调用方法成功"),
    FAIL(500, "调用方法失败"),
    METHOD_NOT_FOUND(210, "未找到指定方法"),
    CLASS_NOT_FOUND(220, "未找到指定类");

    private final int code;
    private final String message;
}
