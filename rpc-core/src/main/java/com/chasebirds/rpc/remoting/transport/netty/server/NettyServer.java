package com.chasebirds.rpc.remoting.transport.netty.server;


import com.chasebirds.rpc.config.CustomShutdownHook;
import com.chasebirds.rpc.provider.ServiceProvider;
import com.chasebirds.rpc.provider.ServiceProviderImpl;
import com.chasebirds.rpc.registry.ServiceRegistry;
import com.chasebirds.rpc.registry.ZkServiceRegistry;
import com.chasebirds.rpc.remoting.dto.RpcRequest;
import com.chasebirds.rpc.remoting.dto.RpcResponse;
import com.chasebirds.rpc.remoting.transport.netty.codec.kyro.NettyKryoDecoder;
import com.chasebirds.rpc.remoting.transport.netty.codec.kyro.NettyKryoEncoder;
import com.chasebirds.rpc.serialize.kyro.KryoSerializer;
import com.chasebirds.rpc.utils.zk.CuratorClient;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * 服务端。接收客户端消息，并且根据客户端的消息调用相应的方法，然后返回结果给客户端。
 *
 * @author 杜强
 * @createTime 2020年06月22日
 */
@Slf4j
public class NettyServer {
    private String host;
    private int port;
    private KryoSerializer kryoSerializer;
    private ServiceRegistry serviceRegistry;
    private ServiceProvider serviceProvider;
    private CuratorClient curatorClient;

    private EventLoopGroup bossGroup =   new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    public NettyServer(CuratorClient curatorClient, String host, int port) {
        this.curatorClient = curatorClient;
        this.host = host;
        this.port = port;
        kryoSerializer = new KryoSerializer();
        serviceRegistry = new ZkServiceRegistry(curatorClient);
        serviceProvider = new ServiceProviderImpl();
    }

    public <T> void publishService(String interfaceName, Class<T> serviceClass) {
        serviceProvider.addServiceProvider(interfaceName, serviceClass);
        serviceRegistry.registerService(interfaceName, new InetSocketAddress(host, port));
        start();
    }

    private void start() {
        CustomShutdownHook.getCustomShutdownHook().clearAll(curatorClient);
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    // 当客户端第一次进行请求的时候才会进行初始化
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new NettyKryoDecoder(kryoSerializer, RpcRequest.class));
                            ch.pipeline().addLast(new NettyKryoEncoder(kryoSerializer, RpcResponse.class));
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    })
                    // TCP默认开启了 Nagle 算法，该算法的作用是尽可能的发送大数据快，减少网络传输。TCP_NODELAY 参数的作用就是控制是否启用 Nagle 算法。
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    // 是否开启 TCP 底层心跳机制
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //表示系统用于临时存放已完成三次握手的请求的队列的最大长度,如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数
                    .option(ChannelOption.SO_BACKLOG, 128);

            // 绑定端口，同步等待绑定成功
            ChannelFuture future1 = b.bind(host, port).sync();
//            // 等待服务端监听端口关闭
//            f.channel().closeFuture().sync();
            if (future1.isSuccess()) {
                log.info("连接Netty服务端成功");
            } else {
                log.info("连接失败,进行断线重连");
                future1.channel().eventLoop().schedule(() -> start(), 20, TimeUnit.SECONDS);
            }
        } catch (InterruptedException e) {
            log.error("occur exception when start server:", e);
        }
    }

    public void stop(){
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
