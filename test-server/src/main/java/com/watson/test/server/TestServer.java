package com.watson.test.server;

import com.watson.rpc.api.HelloService;
import com.watson.rpc.registry.DefaultServiceRegistry;
import com.watson.rpc.registry.ServiceRegistry;
import com.watson.rpc.server.RpcServer;

/**
 * @author watson
 */
public class TestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        RpcServer rpcServer = new RpcServer(serviceRegistry);
        rpcServer.start(9000);
    }
}
