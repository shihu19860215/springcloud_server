package com.msh.gateway.server.service;

import com.msh.fastdevelop.sys.client.po.AuthorityUrlPO;
import com.msh.fastdevelop.sys.client.qo.AuthorityUrlQO;
import com.msh.frame.client.common.CommonResult;
import com.msh.frame.client.common.Page;
import com.msh.gateway.server.client.SysAuthorityUrlClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysAuthorityService {
    @Autowired
    private SysAuthorityUrlClient sysAuthorityUrlClient;
    public CommonResult<Map<String, Integer>> getUrlAuthIdMap(){
        AuthorityUrlQO authorityUrlQO = new AuthorityUrlQO();
        authorityUrlQO.getPage().setPageSize(Integer.MAX_VALUE);
        CommonResult<List<AuthorityUrlPO>> listCommonResult
                = sysAuthorityUrlClient.list(authorityUrlQO);
        if(0 != listCommonResult.getCode()){
            return CommonResult.errorReturn(listCommonResult.getCode(),listCommonResult.getMessage());
        }
        List<AuthorityUrlPO> authorityUrlPOList = listCommonResult.getResult();
        Map<String, Integer> map ;
        if(null == authorityUrlPOList){
            map = Collections.emptyMap();
        }else {
            map = new HashMap<>(authorityUrlPOList.size()*2);
            authorityUrlPOList.stream().forEach(a->{
                map.put(a.getUrl(),a.getLinkAuth());
            });
        }
        return CommonResult.successReturn(map);
    }
}
