package com.watson.example;

import com.watson.rpc.api.HelloService;
import com.watson.rpc.config.RpcServiceConfig;
import com.watson.rpc.remote.transport.RpcServer;
import com.watson.rpc.remote.transport.netty.server.NettyRpcServer;


/**
 * 测试用Netty服务提供者（服务端）
 *
 * @author watson
 */
public class NettyTestServerMain2 {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        RpcServer nettyRpcServer = new NettyRpcServer(9998);
        RpcServiceConfig rpcServiceConfig = RpcServiceConfig.builder()
                .group("netty").version("version1").service(helloService).build();
        nettyRpcServer.registerService(rpcServiceConfig);
        nettyRpcServer.start();
    }
}
