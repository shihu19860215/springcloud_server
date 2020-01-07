package com.msh.gateway.server.client;

import com.msh.artascope.sys.client.po.AuthorityUrlPO;
import com.msh.artascope.sys.client.qo.AuthorityUrlQO;
import com.msh.frame.client.common.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "artascope-sys-api",path = "/authorityurl")
public interface SysAuthorityUrlClient {

    @RequestMapping(value = "/list",method = RequestMethod.POST)
    CommonResult<List<AuthorityUrlPO>> list(@RequestBody AuthorityUrlQO var1);
}
