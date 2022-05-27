package com.watson.test;

import com.watson.rpc.api.HelloObject;
import com.watson.rpc.transport.RpcServer;
import com.watson.rpc.api.HelloService;
import com.watson.rpc.serializer.Hessian2Serializer;
import com.watson.rpc.transport.socket.server.SocketServer;

/**
 * @author watson
 */
public class SocketTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        RpcServer socketServer = new SocketServer("127.0.0.1", 9000);
        socketServer.setSerializer(new Hessian2Serializer());
        socketServer.publishService(helloService, HelloObject.class);
    }
}
