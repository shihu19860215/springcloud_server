package com.msh.gateway.server.client;

import com.msh.fastdevelop.sys.client.po.AuthorityUrlPO;
import com.msh.fastdevelop.sys.client.qo.AuthorityUrlQO;
import com.msh.frame.client.common.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "sys-api",path = "/authorityurl")
public interface SysAuthorityUrlClient {

    @RequestMapping(value = "/list",method = RequestMethod.POST)
    CommonResult<List<AuthorityUrlPO>> list(AuthorityUrlQO var1);
}
