package com.watson.rpc.serializer;

/**
 * 通用的序列化反序列化接口
 *
 * @author watson
 */
public interface CommonSerializer {
    static CommonSerializer getByCode(int code) {
        switch (code) {
            case 0:
                return new KryoSerializer();
            case 1:
                return new JsonSerializer();
            case 2:
                return new Hessian2Serializer();
            case 3:
                return new ProtobufSerializer();
            default:
                return null;
        }
    }

    byte[] serialize(Object obj);

    Object deserialize(byte[] bytes, Class<?> clazz);

    int getCode();
}
