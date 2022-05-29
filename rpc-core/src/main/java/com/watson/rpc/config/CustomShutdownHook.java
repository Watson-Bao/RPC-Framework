package com.watson.rpc.config;

import com.watson.rpc.registry.nacos.utils.NacosUtil;
import com.watson.rpc.utils.concurrent.threadpool.ThreadPoolFactoryUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 当服务端（provider）关闭的时候做一些事情比如取消注册所有服务
 *
 * @author watson
 */
@Slf4j
public class CustomShutdownHook {
    private static final CustomShutdownHook CUSTOM_SHUTDOWN_HOOK = new CustomShutdownHook();

    public static CustomShutdownHook getCustomShutdownHook() {
        return CUSTOM_SHUTDOWN_HOOK;
    }

    public void clearAll() {
        log.info("addShutdownHook for clearAll");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            NacosUtil.clearRegistry();
            ThreadPoolFactoryUtil.shutDownAllThreadPool();
        }));
    }
}
