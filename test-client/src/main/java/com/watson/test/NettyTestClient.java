package com.watson.test;

import com.watson.rpc.config.RpcServiceConfig;
import com.watson.rpc.transport.RpcClient;
import com.watson.rpc.transport.RpcClientProxy;
import com.watson.rpc.api.HelloObject;
import com.watson.rpc.api.HelloService;
import com.watson.rpc.transport.netty.client.NettyRpcClient;
import com.watson.rpc.serializer.Hessian2Serializer;


/**
 * 测试用Netty消费者
 *
 * @author watson
 */
public class NettyTestClient {
    public static void main(String[] args) {
        RpcClient client = new NettyRpcClient();
        client.setSerializer(new Hessian2Serializer());
        RpcServiceConfig rpcServiceConfig=RpcServiceConfig.builder().group("netty").version("version1").build();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client,rpcServiceConfig);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object, "Netty---");
        System.out.println(res);


    }
}
