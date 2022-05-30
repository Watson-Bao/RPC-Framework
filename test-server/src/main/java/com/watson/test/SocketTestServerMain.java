package com.watson.test;

import com.watson.rpc.api.HelloService;
import com.watson.rpc.config.RpcServiceConfig;
import com.watson.rpc.remote.transport.RpcServer;
import com.watson.rpc.remote.transport.socket.server.SocketRpcServer;
import com.watson.rpc.serializer.JsonSerializer;

/**
 * @author watson
 */
public class SocketTestServerMain {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl2();
        RpcServer socketRpcServer = new SocketRpcServer(9000, new JsonSerializer());
        RpcServiceConfig rpcServiceConfig = RpcServiceConfig.builder()
                .group("socket").version("version2").service(helloService).build();
        socketRpcServer.registerService(rpcServiceConfig);
        socketRpcServer.start();
    }
}
