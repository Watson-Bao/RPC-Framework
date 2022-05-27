package com.watson.rpc;

import com.watson.rpc.serializer.CommonSerializer;

/**
 * 服务器类通用接口
 *
 * @author watson
 */
public interface RpcServer {
    /**
     * 服务器端开始监听请求
     *
     * @param port
     */
    void start(int port);

    /**
     * 设置序列化器
     *
     * @param serializer
     */
    void setSerializer(CommonSerializer serializer);
}
