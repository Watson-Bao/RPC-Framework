package com.watson.rpc;

/**
 * 服务器类通用接口
 * @author watson
 */
public interface RpcServer {
    /**
     * 服务器端开始监听请求
     * @param port
     */
    void start(int port);
}
