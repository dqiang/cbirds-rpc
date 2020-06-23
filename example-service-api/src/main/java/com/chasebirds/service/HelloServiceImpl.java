package com.chasebirds.service;

import com.chasebirds.rpc.annotation.RpcService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 杜强
 * @createTime 2020年06月21日
 */
@Slf4j
@RpcService(HelloService.class)
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(Hello hello) {
        log.info("HelloServiceImpl收到: {}.", hello.getMessage());
        String result = "Hello description is " + hello.getDescription();
        log.info("HelloServiceImpl返回: {}.", result);
        return result;
    }
}
