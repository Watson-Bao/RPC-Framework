package com.watson.rpc.loadblance;

import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 轮询负载均衡
 *
 * @author watson
 */
@Slf4j
public class RoundRobinLoadBalance extends AbstractLoadBalance {
    private int index = 0;

    @Override
    protected Instance doSelect(List<Instance> instances) {
        log.info("轮询负载均衡选择");
        if (index >= instances.size()) {
            index %= instances.size();
        }
        return instances.get(index++);
    }
}
