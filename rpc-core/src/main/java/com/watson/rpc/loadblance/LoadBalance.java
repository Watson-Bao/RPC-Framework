package com.watson.rpc.loadblance;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * 负载均衡接口
 * @author watson
 */
public interface LoadBalance {
    /**
     * 在已有服务实例（包含地址信息）列表中选择一个
     *
     * @param instances 服务实例（包含地址信息）列表
     * @return 目标服务地址
     */
    Instance selectServiceAddress(List<Instance> instances);
}
