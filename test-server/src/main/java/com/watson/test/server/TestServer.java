package com.watson.test.server;

import com.watson.rpc.api.HelloObject;
import com.watson.rpc.api.HelloService;
import com.watson.rpc.server.RpcServer;

/**
 * @author watson
 */
public class TestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
//        HelloObject helloObject=new HelloObject(111,"test");
//        System.out.println(helloService.hello(helloObject));
        RpcServer rpcServer = new RpcServer();
        rpcServer.register(helloService, 9000);
    }
}
