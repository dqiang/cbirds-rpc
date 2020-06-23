package com.chasebirds.rpc.provider;

/**
 * 保存和提供服务实例对象。服务端使用。
 *
 * @author 杜强
 * @createTime 2020年06月22日
 */
public interface ServiceProvider {

    /**
     * 保存服务实例对象和服务实例对象实现的接口类的对应关系
     *
     * @param service      服务实例对象
     * @param serviceClass 服务实例对象实现的接口类
     * @param <T>          服务接口的类型
     */
    <T> void addServiceProvider(String interfaceName, Class<T> serviceClass);

    /**
     * 获取服务实例对象
     *
     * @param serviceName 服务实例对象实现的接口类的类名
     * @return 服务实例对象
     */
    Class<?> getServiceProvider(String serviceName);
}
