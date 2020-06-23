package com.chasebirds.service;

import lombok.*;

import java.io.Serializable;

/**
 * @author 杜强
 * @createTime 2020年06月21日
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Hello implements Serializable {
    private String message;
    private String description;
}
