package com.watson.rpc.serializer;

import com.watson.rpc.enume.SerializerEnum;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 使用ProtoBuf的序列化器
 *
 * @author watson
 */
public class ProtobufSerializer implements CommonSerializer {
    private LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
    private Map<Class<?>, Schema<?>> schemaCache = new ConcurrentHashMap<>();

    @Override
    public byte[] serialize(Object obj) {
        Class clazz = obj.getClass();
        Schema schema = getSchema(clazz);
        byte[] data;
        try {
            data = ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } finally {
            buffer.clear();
        }
        return data;
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        Schema schema = getSchema(clazz);
        Object obj = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(bytes, obj, schema);
        return clazz.cast(obj);
    }

    @Override
    public byte getCode() {
        return SerializerEnum.PROTOBUF.getCode();
    }

    private Schema getSchema(Class clazz) {
        Schema schema = schemaCache.get(clazz);
        if (Objects.isNull(schema)) {
            // 这个schema通过RuntimeSchema进行懒创建并缓存
            // 所以可以一直调用RuntimeSchema.getSchema(),这个方法是线程安全的
            schema = RuntimeSchema.getSchema(clazz);
            if (Objects.nonNull(schema)) {
                schemaCache.put(clazz, schema);
            }
        }
        return schema;
    }
}
