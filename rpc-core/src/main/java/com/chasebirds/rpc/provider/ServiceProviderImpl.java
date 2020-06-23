package com.chasebirds.rpc.provider;


import com.chasebirds.rpc.enumeration.RpcErrorMessageEnum;
import com.chasebirds.rpc.exception.RpcException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.reflect.FastClass;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 实现了 ServiceProvider 接口，可以将其看做是一个保存和提供服务实例对象的示例
 *
 * @author 杜强
 * @createTime 2020年06月22日
 */
@Slf4j
public class ServiceProviderImpl implements ServiceProvider {

    /**
     * 接口名和服务的对应关系
     * note:处理一个接口被两个实现类实现的情况如何处理？（通过 group 分组）
     * key:service/interface name
     * value:service
     */
    private static Map<String, Class<?> > serviceMap = new ConcurrentHashMap<>();
    private static Set<String> registeredService = ConcurrentHashMap.newKeySet();

    /**
     * note:可以修改为扫描注解注册
     */
    @Override
    public <T> void addServiceProvider(String interfaceName, Class<T> serviceClass) {
        String serviceName = serviceClass.getCanonicalName();
        if (registeredService.contains(serviceName)) {
            return;
        }
        registeredService.add(serviceName);
        serviceMap.put(interfaceName, serviceClass);
        log.info("Add service: {} and interfaces:{}", serviceName, serviceClass.getInterfaces());
    }

    @Override
    public  Class<?> getServiceProvider(String serviceName) {
        Class<?> serviceClass = serviceMap.get(serviceName);
//        FastClass serviceFastClass = FastClass.create(serviceClass);
        if (null == serviceClass) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND);
        }
        return serviceClass;
    }
}
