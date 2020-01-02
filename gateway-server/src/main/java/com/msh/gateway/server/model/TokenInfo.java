package com.msh.gateway.server.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class TokenInfo implements Serializable {
    private Long userId;
    private Long expire;

    public TokenInfo(Long userId, Long expire) {
        this.userId = userId;
        this.expire = expire;
    }

    public TokenInfo() {
    }
}
