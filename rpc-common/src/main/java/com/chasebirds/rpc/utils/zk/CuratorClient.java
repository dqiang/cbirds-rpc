package com.chasebirds.rpc.utils.zk;

import com.chasebirds.rpc.config.CuratorProperties;
import com.chasebirds.rpc.config.RpcProperties;
import com.chasebirds.rpc.enumeration.RpcErrorMessageEnum;
import com.chasebirds.rpc.exception.RpcException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class CuratorClient {

    @Autowired
    private CuratorProperties curatorProperties;

    private CuratorFramework curatorFramework;

    private Map<String, List<String>> serviceAddressMap = new ConcurrentHashMap<>();
    private Set<String> registeredPathSet = ConcurrentHashMap.newKeySet();

    CuratorClient(CuratorProperties curatorProperties){
        this.curatorProperties = curatorProperties;
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(curatorProperties.getRetrySleetime(),curatorProperties.getRetryTimes());
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(curatorProperties.getServerAddr())
                .retryPolicy(retryPolicy)
                .build();
        curatorFramework.start();
    }

    CuratorFramework getCuratorFramework(){
        return this.curatorFramework;
    }

    /**
     * 创建服务端持久化节点
     * @author dqiang
     * @param serviceName
     */
    @SneakyThrows
    public void createPersistentNode(String serviceName, InetSocketAddress inetSocketAddress)  {

        String path = curatorProperties.getRegisterPath() + "/" + serviceName + inetSocketAddress.toString();
        try{
            if (registeredPathSet.contains(path) || curatorFramework.checkExists().forPath(path) != null){
                log.info("Service Node is exist: [{}]",path);
            }else{

                curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
            }
            registeredPathSet.add(path);
        }catch (Exception e){
            throw new RpcException(e.getMessage(), e.getCause());
        }
    }

    /**
     * 清空注册的服务
     * @author dqiang
     */
    public void clearRegistry(){
        registeredPathSet.stream().parallel().forEach(p->{
            try {
                curatorFramework.delete().forPath(p);
            }catch (Exception e){

            }
        });
    }

    /**
     * 获取服务可用地址列表
     * @param serviceName
     * @return
     */
    public List<String> getChildrenNodes(String serviceName){
        if (serviceAddressMap.containsKey(serviceName)){
            return serviceAddressMap.get(serviceName);
        }
        List<String> result = new ArrayList<>();
        String servicePath = curatorProperties.getRegisterPath() + "/" + serviceName;
        try{
            result = curatorFramework.getChildren().forPath(servicePath);
            serviceAddressMap.put(serviceName,result);
            registerWatcher(serviceName);
        }catch (Exception e){
             throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND);
        }

        if (result.size() == 0){
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND);
        }
        return result;
    }


    private void registerWatcher(String serviceName){
        String servicePath = curatorProperties.getRegisterPath() + "/" + serviceName;
        PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework,servicePath,true);
        PathChildrenCacheListener pathChildrenCacheListener = (zkclient,event)->{
            List<String> serverAddress = zkclient.getChildren().forPath(servicePath);
            serviceAddressMap.put(serviceName,serverAddress);
        };
        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
    }
}
