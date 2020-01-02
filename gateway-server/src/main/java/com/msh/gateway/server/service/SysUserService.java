package com.msh.gateway.server.service;

import com.msh.fastdevelop.sys.client.model.UserInfo;
import com.msh.fastdevelop.sys.client.qo.UserQO;
import com.msh.frame.client.common.CommonResult;
import com.msh.frame.interfaces.ICache;
import com.msh.frame.interfaces.IdGenerateable;
import com.msh.gateway.server.client.SysUserClinet;
import com.msh.gateway.server.model.TokenInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class SysUserService {
    @Autowired
    private SysUserClinet sysUserClinet;

    public CommonResult<UserInfo> login(String username, String password){
        return sysUserClinet.getUserInfo(username, password);
    }
}
