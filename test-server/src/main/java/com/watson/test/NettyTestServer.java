package com.watson.test;

import com.watson.rpc.api.HelloService;
import com.watson.rpc.config.RpcServiceConfig;
import com.watson.rpc.remote.transport.RpcServer;
import com.watson.rpc.remote.transport.netty.server.NettyRpcServer;
import com.watson.rpc.serializer.Hessian2Serializer;
import com.watson.rpc.serializer.ProtobufSerializer;


/**
 * 测试用Netty服务提供者（服务端）
 *
 * @author watson
 */
public class NettyTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        RpcServer nettyRpcServer = new NettyRpcServer(9999);
        nettyRpcServer.setSerializer(new Hessian2Serializer());
        RpcServiceConfig rpcServiceConfig = RpcServiceConfig.builder()
                .group("netty").version("version1").service(helloService).build();
        nettyRpcServer.registerService(rpcServiceConfig);
        nettyRpcServer.start();
    }
}
