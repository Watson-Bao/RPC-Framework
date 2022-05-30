package com.watson.rpc.loadblance;

import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Random;

/**
 * 随机负载均衡
 *
 * @author watson
 */
@Slf4j
public class RandomLoadBalance extends AbstractLoadBalance {
    @Override
    protected Instance doSelect(List<Instance> instances) {
        log.info("随机负载均衡选择");
        Random random = new Random();
        return instances.get(random.nextInt(instances.size()));
    }
}
