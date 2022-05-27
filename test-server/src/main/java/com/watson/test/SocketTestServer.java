package com.watson.test;

import com.watson.rpc.api.HelloService;
import com.watson.rpc.registry.DefaultServiceRegistry;
import com.watson.rpc.registry.ServiceRegistry;
import com.watson.rpc.serializer.HessianSerializer;
import com.watson.rpc.socket.server.SocketServer;

/**
 * @author watson
 */
public class SocketTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        SocketServer socketServer = new SocketServer(serviceRegistry);
        socketServer.setSerializer(new HessianSerializer());
        socketServer.start(9000);
    }
}
