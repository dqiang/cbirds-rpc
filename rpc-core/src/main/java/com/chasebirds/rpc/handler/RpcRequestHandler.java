package com.chasebirds.rpc.handler;

import com.chasebirds.rpc.enumeration.RpcResponseCode;
import com.chasebirds.rpc.exception.RpcException;
import com.chasebirds.rpc.provider.ServiceProvider;
import com.chasebirds.rpc.provider.ServiceProviderImpl;
import com.chasebirds.rpc.remoting.dto.RpcRequest;
import com.chasebirds.rpc.remoting.dto.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * RpcRequest 的处理器
 *
 * @author 杜强
 * @createTime 2020年06月22日
 */
@Slf4j
public class RpcRequestHandler {
    private static ServiceProvider serviceProvider = new ServiceProviderImpl();

    /**
     * 处理 rpcRequest ：调用对应的方法，然后返回方法执行结果
     */
    public Object handle(RpcRequest rpcRequest) {
        //通过注册中心获取到目标类（客户端需要调用类）
        Class<?> service = serviceProvider.getServiceProvider(rpcRequest.getInterfaceName());
        return invokeTargetMethod(rpcRequest, service);
    }

    /**
     * 根据 rpcRequest 和 service 对象特定的方法并返回结果
     *
     * @param rpcRequest 客户端请求
     * @param service    提供服务的对象
     * @return 目标方法执行的结果
     */
    private Object invokeTargetMethod(RpcRequest rpcRequest,  Class<?> service) {
        Object result;
        try {
            Method method = service.getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            if (null == method) {
                return RpcResponse.fail(RpcResponseCode.NOT_FOUND_METHOD);
            }
            result = method.invoke(service.newInstance(), rpcRequest.getParameters());
            log.info("service:[{}] successful invoke method:[{}]", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
            throw new RpcException(e.getMessage(), e);
        }
        return result;
    }
}
