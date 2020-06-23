package com.chasebirds.rpc.remoting.transport;


import com.chasebirds.rpc.remoting.dto.RpcRequest;

/**
 * 传输 RpcRequest。
 *
 * @author 杜强
 * @createTime 2020年06月21日
 */
public interface ClientTransport {
    /**
     * 发送消息到服务端
     *
     * @param rpcRequest 消息体
     * @return 服务端返回的数据
     */
    Object sendRpcRequest(RpcRequest rpcRequest);
}
