package com.watson.rpc.serializer;

import com.watson.rpc.extension.SPI;

/**
 * 通用的序列化反序列化接口
 *
 * @author watson
 */
@SPI
public interface Serializer {

    /**
     * 序列化
     *
     * @param obj
     * @return
     */
    byte[] serialize(Object obj);


    /**
     * 反序列化
     *
     * @param bytes
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);

    /**
     * 获取序列化器对应码
     *
     * @return
     */
    byte getCode();
}
