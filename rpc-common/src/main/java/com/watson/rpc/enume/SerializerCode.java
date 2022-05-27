package com.watson.rpc.enume;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 字节流中标识序列化和反序列化器
 *
 * @author watson
 */
@AllArgsConstructor
@Getter
public enum SerializerCode {
    /**
     * 序列化与反序列化器的枚举
     */
    KRYO(0),
    JSON(1),
    HESSIAN(2);

    private final int code;
}
