package com.watson.test;

import com.watson.rpc.transport.RpcClient;
import com.watson.rpc.transport.RpcClientProxy;
import com.watson.rpc.api.HelloObject;
import com.watson.rpc.api.HelloService;
import com.watson.rpc.transport.netty.client.NettyClient;
import com.watson.rpc.serializer.Hessian2Serializer;


/**
 * 测试用Netty消费者
 *
 * @author watson
 */
public class NettyTestClient {
    public static void main(String[] args) {
        RpcClient client = new NettyClient();
        client.setSerializer(new Hessian2Serializer());
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object, "111111111111111111111");
        System.out.println(res);


    }
}
