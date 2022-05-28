package com.watson.test;

import com.watson.rpc.config.RpcServiceConfig;
import com.watson.rpc.transport.RpcServer;
import com.watson.rpc.api.HelloService;
import com.watson.rpc.serializer.Hessian2Serializer;
import com.watson.rpc.transport.socket.server.SocketRpcServer;

/**
 * @author watson
 */
public class SocketTestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl2();
        RpcServer socketRpcServer = new SocketRpcServer("127.0.0.1", 9000);
        socketRpcServer.setSerializer(new Hessian2Serializer());
        RpcServiceConfig rpcServiceConfig = RpcServiceConfig.builder()
                .group("socket").version("version2").service(helloService).build();
        socketRpcServer.registerService(rpcServiceConfig);
        socketRpcServer.start();
    }
}
