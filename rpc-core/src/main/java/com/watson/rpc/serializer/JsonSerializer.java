package com.watson.rpc.serializer;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.watson.rpc.enume.SerializerEnum;
import com.watson.rpc.remote.dto.RpcRequest;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;


/**
 * 使用JSON格式的序列化器
 *
 * @author watson
 */
@Slf4j
public class JsonSerializer implements Serializer {

    @Override
    public byte[] serialize(Object obj) {
        return JSON.toJSONString(obj).getBytes();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        Object obj = JSON.parseObject(new String(bytes), clazz, JSONReader.Feature.SupportClassForName);
        if (obj instanceof RpcRequest) {
            obj = handleRequest(obj);
        }
        log.info("deserialized---" + obj.toString());
        return clazz.cast(obj);
    }

    /**
     * 这里由于使用FASTJSON序列化和反序列化Object数组，无法保证反序列化后仍然为原实例类型(数组内部是JsonObject)
     * 需要重新判断处理
     */
    private Object handleRequest(Object obj) {
        RpcRequest rpcRequest = (RpcRequest) obj;
        for (int i = 0; i < rpcRequest.getParamTypes().length; i++) {
            Class<?> innerClazz = rpcRequest.getParamTypes()[i];
            if (!innerClazz.isAssignableFrom(rpcRequest.getParameters()[i].getClass())) {
                byte[] bytes = JSON.toJSONString(rpcRequest.getParameters()[i]).getBytes(StandardCharsets.UTF_8);
                rpcRequest.getParameters()[i] = JSON.parseObject(new String(bytes, StandardCharsets.UTF_8), innerClazz);
            }
        }
        return rpcRequest;
    }

    @Override
    public byte getCode() {
        return SerializerEnum.JSON.getCode();
    }
}
