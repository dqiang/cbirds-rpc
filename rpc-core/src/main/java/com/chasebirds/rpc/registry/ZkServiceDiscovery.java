package com.chasebirds.rpc.registry;

import com.chasebirds.rpc.utils.zk.CuratorClient;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 基于 zookeeper 实现服务发现
 *
 * @author 杜强
 * @createTime 2020年06月22日
 */
@Slf4j
public class ZkServiceDiscovery implements ServiceDiscovery {

    private CuratorClient curatorClient;
    public ZkServiceDiscovery(CuratorClient curatorClient){
        this.curatorClient = curatorClient;
    }

    @Override
    public InetSocketAddress lookupService(String serviceName) {

        // 获取服务器注册相关服务的地址列表
        List<String> serviceAddressList = curatorClient.getChildrenNodes(serviceName);
        Collections.sort(serviceAddressList);

        //采用随机法实现负载均衡的方式
        String serviceAddress = randomLoadBalance(serviceAddressList);
        log.info("成功找到服务地址:{}", serviceAddress);

        String[] socketAddressArray = serviceAddress.split(":");
        String host = socketAddressArray[0];
        int port = Integer.parseInt(socketAddressArray[1]);
        return new InetSocketAddress(host, port);
    }

    private String randomLoadBalance( List<String> vlist){
        Random random=new Random();
        int randomPos = random.nextInt(vlist.size());

        String server = vlist.get(randomPos);
        return server;
    }
}
