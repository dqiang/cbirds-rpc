package com.chasebirds.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author 杜强
 * @createTime 2020年06月22日
 */
@Getter
@ToString
public enum RpcErrorMessageEnum implements CodeEnumInterface{

    CLIENT_CONNECT_SERVER_FAILURE(3001,"客户端连接服务端失败"),
    SERVICE_INVOCATION_FAILURE(3002,"服务调用失败"),
    SERVICE_CAN_NOT_BE_FOUND(3003,"没有找到指定的服务"),
    SERVICE_NOT_IMPLEMENT_ANY_INTERFACE(3004,"注册的服务没有实现任何接口"),
    REQUEST_NOT_MATCH_RESPONSE(3005,"返回结果错误！请求和返回的相应不匹配");

    /**
     * 错误码
     */
    private Integer code;
    /**
     * 错误码消息
     */
    private String msg;

    RpcErrorMessageEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
