package com.watson.rpc.registry.nacos.utils;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.watson.rpc.config.RpcServiceConfig;
import com.watson.rpc.enume.RpcError;
import com.watson.rpc.exception.RpcException;
import com.watson.rpc.remote.to.RpcRequest;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 管理Nacos连接等工具类
 *
 * @author watson
 */
@Slf4j
public class NacosUtil {
    private static final String SERVER_ADDR = "wslhost:8848";

    public static NamingService getNacosNamingService() {
        try {
            return NamingFactory.createNamingService(SERVER_ADDR);
        } catch (NacosException e) {
            log.error("连接到Nacos时有错误发生: ", e);
            throw new RpcException(RpcError.FAILED_TO_CONNECT_TO_SERVICE_REGISTRY);
        }
    }

    public static void registerService(NamingService namingService, RpcServiceConfig rpcServiceConfig, InetSocketAddress address) throws NacosException {

        namingService.registerInstance(rpcServiceConfig.getRpcServiceName(), rpcServiceConfig.getGroup(), address.getHostName(), address.getPort());
    }

    public static List<Instance> getAllInstance(NamingService namingService, RpcRequest rpcRequest) throws NacosException {
        return namingService.getAllInstances(rpcRequest.getRpcServiceName(), rpcRequest.getGroup());
    }
}
