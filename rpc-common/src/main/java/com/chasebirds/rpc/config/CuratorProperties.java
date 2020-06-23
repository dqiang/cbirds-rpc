package com.chasebirds.rpc.config;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PropertyKey;
import java.util.regex.Pattern;

@Data
@Component
@ConfigurationProperties(prefix = "cbirds.zk")
public class CuratorProperties {

    private static final Logger log = LoggerFactory
            .getLogger(CuratorProperties.class);

    public static final String PREFIX = "cbirds.zk";

    private static final Pattern PATTERN = Pattern.compile("-(\\w)");

    private String serverAddr;

    private Integer retryTimes;

    private Integer retrySleetime;

    private String registerPath;

    @Value("${server.port}")
    private Integer port;

    @PostConstruct
    public void init(){

    }
}
