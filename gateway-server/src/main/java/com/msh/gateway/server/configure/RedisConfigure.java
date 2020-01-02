package com.msh.gateway.server.configure;

import com.msh.fastdevelop.sys.client.model.UserInfo;
import com.msh.frame.interfaces.ICache;
import com.msh.frame.interfaces.ICacheManager;
import com.msh.gateway.server.model.TokenInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfigure{
    @Bean("userInfoCache")
    public ICache<Long,UserInfo> cacheUserInfo(ICacheManager cacheManager){
        return cacheManager.getCache("com.msh.gateway.server:userInfoCache:");
    }
    @Bean("tokenCache")
    public ICache<Long, TokenInfo> cacheToken(ICacheManager cacheManager){
        return cacheManager.getCache("com.msh.gateway.server:tokenCache:");
    }
}
