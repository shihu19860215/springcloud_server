package com.msh.gateway.server.client;

import com.msh.artascope.sys.client.model.UserInfo;
import com.msh.frame.client.common.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "artascope-sys-api",path = "/user")
public interface SysUserClinet {
    @RequestMapping(value = "/listuserinfo",method = RequestMethod.GET)
    public CommonResult<UserInfo> getUserInfo(@RequestParam("username")String username, @RequestParam("password")String password);
}
