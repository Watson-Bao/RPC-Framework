package com.watson.rpc.transport;

import com.watson.rpc.serializer.CommonSerializer;

/**
 * 服务器类通用接口
 *
 * @author watson
 */
public interface RpcServer {
    /**
     * 服务器端开始监听请求
     */
    void start();

    /**
     * 设置序列化器
     *
     * @param serializer
     */
    void setSerializer(CommonSerializer serializer);

    /**
     * 服务器端发布服务
     * @param service
     * @param serviceClass
     * @param <T>
     */
    <T> void publishService(Object service, Class<T> serviceClass);
}
