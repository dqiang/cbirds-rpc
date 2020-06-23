package com.chasebirds;

import com.chasebirds.rpc.annotation.EnableRpcServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRpcServer
@SpringBootApplication
public class ServerApp {

    public static void main(String[] args){
        SpringApplication.run(ServerApp.class, args);
    }
}
