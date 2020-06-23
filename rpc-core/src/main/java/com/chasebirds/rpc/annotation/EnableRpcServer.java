package com.chasebirds.rpc.annotation;

import com.chasebirds.rpc.config.RpcServerAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(RpcServerAutoConfiguration.class)
public @interface EnableRpcServer {
}
