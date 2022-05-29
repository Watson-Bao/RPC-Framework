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
public enum SerializerEnum {
    /**
     * 序列化与反序列化器的枚举
     */
    HESSIAN2((byte) 0x01, "HESSIAN2"),
    JSON((byte) 0x02, "JSON"),
    KRYO((byte) 0x03, "KRYO"),
    PROTOBUF((byte) 0x04, "PROTOBUF");


    private final byte code;
    private final String name;

    public static String getName(byte code) {
        for (SerializerEnum s : SerializerEnum.values()) {
            if (s.getCode() == code) {
                return s.name;
            }
        }
        return null;
    }
}
