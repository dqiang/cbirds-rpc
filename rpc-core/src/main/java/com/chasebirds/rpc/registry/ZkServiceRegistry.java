package com.chasebirds.rpc.registry;

import com.chasebirds.rpc.utils.zk.CuratorClient;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * 基于 zookeeper 实现服务注册
 *
 * @author 杜强
 * @createTime 2020年06月22日
 */
@Slf4j
public class ZkServiceRegistry implements ServiceRegistry {

    private CuratorClient curatorClient;
    public ZkServiceRegistry(CuratorClient curatorClient){
        this.curatorClient = curatorClient;
    }

    @Override
    public void registerService(String serviceName, InetSocketAddress inetSocketAddress) {
        //根节点下注册子节点：服务
        curatorClient.createPersistentNode(serviceName,inetSocketAddress);
    }
}
