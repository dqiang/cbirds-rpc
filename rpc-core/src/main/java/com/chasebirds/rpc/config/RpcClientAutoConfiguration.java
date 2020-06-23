package com.chasebirds.rpc.config;

import com.chasebirds.rpc.proxy.RpcClientProxy;
import com.chasebirds.rpc.remoting.transport.ClientTransport;
import com.chasebirds.rpc.remoting.transport.netty.client.NettyClientTransport;
import com.chasebirds.rpc.utils.zk.CuratorClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableAutoConfiguration
public class RpcClientAutoConfiguration {

    @Autowired
    private CuratorClient curatorClient;

    @Bean
    public RpcClientProxy rpcClientProxy(){
        ClientTransport rpcClient = new NettyClientTransport(curatorClient);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcClient);
        return rpcClientProxy;
    }
}
