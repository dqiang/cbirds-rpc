package com.chasebirds.rpc.registry;

import java.net.InetSocketAddress;

/**
 * 服务注册接口
 *
 * @author 杜强
 * @createTime 2020年06月22日
 */
public interface ServiceRegistry {
    /**
     * 注册服务
     *
     * @param serviceName       服务名称
     * @param inetSocketAddress 提供服务的地址
     */
    void registerService(String serviceName, InetSocketAddress inetSocketAddress);

}
