package com.chasebirds;

import com.chasebirds.rpc.proxy.RpcClientProxy;
import com.chasebirds.service.Hello;
import com.chasebirds.service.HelloService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestApp {

    @Autowired
    private RpcClientProxy rpcClientProxy;

    @Test
    public void test(){
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        String hello = helloService.hello(new Hello("111", "222"));
        //如需使用 assert 断言，需要在 VM options 添加参数：-ea
        assert "Hello description is 222".equals(hello);
        for (int i = 0; i < 50; i++) {
            String des = helloService.hello(new Hello("111", "~~~" + i));
            System.out.println(des);
        }
    }
}
