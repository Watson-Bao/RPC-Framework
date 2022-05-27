package com.watson.test;

import com.watson.rpc.RpcClient;
import com.watson.rpc.RpcClientProxy;
import com.watson.rpc.api.HelloObject;
import com.watson.rpc.api.HelloService;
import com.watson.rpc.netty.client.NettyClient;

/**
 * 测试用Netty消费者
 *
 * @author watson
 */
public class NettyTestClient {
    public static void main(String[] args) {
        RpcClient client = new NettyClient("127.0.0.1", 9999);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object, "111111111111111111111");
        System.out.println(res);


    }
}
