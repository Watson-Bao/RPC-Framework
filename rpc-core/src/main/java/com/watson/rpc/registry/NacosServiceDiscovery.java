package com.watson.rpc.registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.watson.rpc.config.RpcServiceConfig;
import com.watson.rpc.entity.RpcRequest;
import com.watson.rpc.registry.utils.NacosUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;
/**
 * @author watson
 */
@Slf4j
public class NacosServiceDiscovery implements ServiceDiscovery {

    private final NamingService namingService;

    public NacosServiceDiscovery() {
        namingService = NacosUtil.getNacosNamingService();
    }
    /**
     * 根据服务名称查找服务实体连接地址
     *
     * @param rpcRequest rpc请求信息
     * @return 服务实体连接地址
     */
    @Override
    public InetSocketAddress lookupService(RpcRequest rpcRequest) {
        try {
            List<Instance> instances = NacosUtil.getAllInstance(namingService, rpcRequest);
            Instance instance = instances.get(0);
            return new InetSocketAddress(instance.getIp(), instance.getPort());
        } catch (NacosException e) {
            log.error("获取服务时有错误发生:", e);
        }
        return null;
    }
}
