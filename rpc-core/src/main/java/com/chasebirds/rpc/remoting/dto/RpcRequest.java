package com.chasebirds.rpc.remoting.dto;

import lombok.*;

import java.io.Serializable;

/**
 * @author 杜强
 * @createTime 2020年06月22日
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = 1905122041950251207L;
    private String requestId;
    private String interfaceName;
    private String methodName;
    private Object[] parameters;
    private Class<?>[] paramTypes;
}
