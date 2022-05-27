package com.watson.test;

import com.watson.rpc.RpcServer;
import com.watson.rpc.api.HelloService;
import com.watson.rpc.netty.server.NettyServer;
import com.watson.rpc.registry.DefaultServiceRegistry;
import com.watson.rpc.registry.ServiceRegistry;
import com.watson.rpc.serializer.KryoSerializer;

/**
 * 测试用Netty服务提供者（服务端）
 *
 * @author watson
 */
public class NettyTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry registry = new DefaultServiceRegistry();
        registry.register(helloService);
        RpcServer server = new NettyServer();
        server.setSerializer(new KryoSerializer());
        server.start(9999);
    }
}
