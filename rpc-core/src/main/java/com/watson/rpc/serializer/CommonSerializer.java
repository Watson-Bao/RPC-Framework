package com.watson.rpc.serializer;

import com.watson.rpc.enume.SerializerEnum;

/**
 * 通用的序列化反序列化接口
 *
 * @author watson
 */
public interface CommonSerializer {
    static CommonSerializer getByCode(byte code) {
        if (code == SerializerEnum.KRYO.getCode()) {
            return new KryoSerializer();
        } else if (code == SerializerEnum.JSON.getCode()) {
            return new JsonSerializer();
        } else if (code == SerializerEnum.HESSIAN2.getCode()) {
            return new Hessian2Serializer();
        } else if (code == SerializerEnum.PROTOBUF.getCode()) {
            return new ProtobufSerializer();
        }
        return null;
    }

    byte[] serialize(Object obj);

    <T> T deserialize(byte[] bytes, Class<T> clazz);

    byte getCode();
}
