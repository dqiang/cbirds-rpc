package com.chasebirds.rpc.config;

import com.chasebirds.rpc.annotation.RpcService;
import com.chasebirds.rpc.remoting.transport.netty.server.NettyServer;
import com.chasebirds.rpc.utils.zk.CuratorClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;

@Slf4j
@ConditionalOnBean(CuratorProperties.class)
public class RpcServerAutoConfiguration {

    @Autowired
    private CuratorClient curatorClient;

    private NettyServer nettyServer;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private RpcProperties rpcProperties;

    @PostConstruct
    public void init() throws Exception{

        nettyServer = new NettyServer(curatorClient,"127.0.0.1",rpcProperties.getPort());

        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        if (MapUtils.isNotEmpty(serviceBeanMap)) {
            for (Object serviceBean : serviceBeanMap.values()) {
                String interfaceName = serviceBean.getClass().getAnnotation(RpcService.class).value().getCanonicalName();
                nettyServer.publishService(interfaceName, serviceBean.getClass());
                log.info("Loading service: {}", interfaceName);
            }
        }
    }

    @PreDestroy
    public void destory() {
        nettyServer.stop();
    }
}
