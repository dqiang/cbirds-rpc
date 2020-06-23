package com.chasebirds.rpc.config;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.regex.Pattern;

@Data
@Component
@ConfigurationProperties(prefix = "cbirds.rpc")
public class RpcProperties {

    private static final Logger log = LoggerFactory
            .getLogger(RpcProperties.class);

    public static final String PREFIX = "cbirds.rpc";

    private static final Pattern PATTERN = Pattern.compile("-(\\w)");

    private Integer port;

    @PostConstruct
    public void init(){

    }
}
