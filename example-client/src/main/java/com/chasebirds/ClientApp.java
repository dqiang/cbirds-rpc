package com.chasebirds;

import com.chasebirds.rpc.proxy.RpcClientProxy;
import com.chasebirds.service.Hello;
import com.chasebirds.service.HelloServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.Method;

@SpringBootApplication
public class ClientApp {

    @Autowired
    private RpcClientProxy rpcClientProxy;

    public static void main(String[] args) {
        try {
            Method method = HelloServiceImpl.class.getMethod("hello", Hello.class);
            System.out.println(method);
        }catch (Exception e){

        }

        SpringApplication.run(ClientApp.class, args);
    }
}
