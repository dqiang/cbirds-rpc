package com.chasebirds.rpc.registry;

import java.net.InetSocketAddress;

/**
 * 服务发现接口
 *
 * @author 杜强
 * @createTime 2020年06月22日
 */
public interface ServiceDiscovery {
    /**
     * 查找服务
     *
     * @param serviceName 服务名称
     * @return 提供服务的地址
     */
    InetSocketAddress lookupService(String serviceName);
}
