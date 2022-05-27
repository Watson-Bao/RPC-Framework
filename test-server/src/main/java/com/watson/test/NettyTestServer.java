package com.watson.test;

import com.watson.rpc.serializer.ProtobufSerializer;
import com.watson.rpc.transport.RpcServer;
import com.watson.rpc.api.HelloService;
import com.watson.rpc.transport.netty.server.NettyServer;


/**
 * 测试用Netty服务提供者（服务端）
 *
 * @author watson
 */
public class NettyTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        RpcServer server = new NettyServer("127.0.0.1", 9999);
        server.setSerializer(new ProtobufSerializer());
        server.publishService(helloService, HelloService.class);
    }
}
