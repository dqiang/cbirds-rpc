package com.chasebirds.rpc.config;

import com.chasebirds.rpc.utils.threadpool.ThreadPoolFactoryUtils;
import com.chasebirds.rpc.utils.zk.CuratorClient;
import lombok.extern.slf4j.Slf4j;

/**
 * 当服务端（provider）关闭的时候取消注册所有服务
 *
 * @author 杜强
 * @createTime 2020年06月22日
 */
@Slf4j
public class CustomShutdownHook {
    private static final CustomShutdownHook CUSTOM_SHUTDOWN_HOOK = new CustomShutdownHook();

    public static CustomShutdownHook getCustomShutdownHook() {
        return CUSTOM_SHUTDOWN_HOOK;
    }

    public void clearAll(CuratorClient curatorClient) {
        log.info("addShutdownHook for clearAll");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            curatorClient.clearRegistry();
            ThreadPoolFactoryUtils.shutDownAllThreadPool();
        }));
    }
}
