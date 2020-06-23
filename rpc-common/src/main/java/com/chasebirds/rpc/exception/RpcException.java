package com.chasebirds.rpc.exception;


import com.chasebirds.rpc.enumeration.RpcErrorMessageEnum;

/**
 * @author duqiang
 * @createTime 2020年06月22日
 */
public class RpcException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private Integer code = 500;
    private String message;

    public RpcException(String msg) {
        super(msg);
        this.message = msg;
    }
    public RpcException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }
    public RpcException(RpcErrorMessageEnum status) {
        super(status.getMsg());
        this.code = status.getCode();
        this.message = status.getMsg();
    }

    public RpcException(RpcErrorMessageEnum rpcErrorMessageEnum, String detail) {
        super(rpcErrorMessageEnum.getMsg() + ":" + detail);
    }

}
