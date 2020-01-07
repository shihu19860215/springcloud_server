package com.msh.gateway.server.service;

import com.msh.frame.client.common.CommonResult;
import com.msh.gateway.server.client.SysUserClinet;
import com.shihu.artascope.sys.client.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserService {
    @Autowired
    private SysUserClinet sysUserClinet;

    public CommonResult<UserInfo> login(String username, String password){
        return sysUserClinet.getUserInfo(username, password);
    }
}
