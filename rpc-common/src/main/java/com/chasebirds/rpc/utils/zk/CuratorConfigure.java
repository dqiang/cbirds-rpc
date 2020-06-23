package com.chasebirds.rpc.utils.zk;

import com.chasebirds.rpc.config.CuratorProperties;
import com.chasebirds.rpc.config.RpcProperties;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@ConditionalOnBean(CuratorProperties.class)
@ConditionalOnClass(CuratorClient.class)
public class CuratorConfigure {

    @Autowired
    private CuratorProperties curatorProperties;

    @Bean
    @ConditionalOnMissingBean(CuratorClient.class)
    public CuratorClient curatorClient(){
        CuratorClient curatorClient = new CuratorClient(curatorProperties);
        return curatorClient;
    }
}
