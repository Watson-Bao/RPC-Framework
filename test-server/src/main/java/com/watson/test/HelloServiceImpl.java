package com.watson.test;

import com.watson.rpc.api.HelloObject;
import com.watson.rpc.api.HelloService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author watson
 */
@Slf4j
public class HelloServiceImpl implements HelloService {
    /**
     * 测试用api的接口
     *
     * @param object
     * @return
     */
    @Override
    public String hello(HelloObject object, String code) {
        log.info("接收到：{}", object.getMessage());
        return code + "这是调用方法的返回值，id=" + object.getId();
    }
}
