package com.msh.gateway.server.filter;


import com.msh.frame.client.common.CommonCode;
import com.msh.frame.client.common.CommonResult;
import com.msh.frame.client.define.StringDef;
import com.msh.frame.interfaces.ICache;
import com.msh.frame.interfaces.IdGenerateable;
import com.msh.gateway.server.model.TokenInfo;
import com.msh.gateway.server.service.SysAuthorityService;
import com.msh.gateway.server.service.SysUserService;
import com.msh.gateway.server.util.ResponseUtil;
import com.msh.starter.id.generate.abstracts.AbstractDateIdGenerate;
import com.msh.starter.id.generate.abstracts.AbstractIdGenerate;
import com.shihu.artascope.sys.client.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class AuthorityWebFilter implements WebFilter,Ordered {
    private static final String AUTHORIZE_TOKEN = "token";
    private static final String AUTHORIZE_UID = "uid";

    @Autowired
    private SysAuthorityService sysAuthorityService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    @Qualifier("userInfoCache")
    private ICache<Long,UserInfo> userInfoCache;
    @Autowired
    @Qualifier("tokenCache")
    private ICache<Long,TokenInfo> tokenCache;
    @Autowired
    private AbstractIdGenerate idGenerateable;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        CommonResult<Map<String, Integer>> urlAuthIdMapCommonResult = sysAuthorityService.getUrlAuthIdMap();
        if(0 != urlAuthIdMapCommonResult.getCode()){
            return ResponseUtil.commonResultResponse(response, urlAuthIdMapCommonResult);
        }
        Map<String, Integer> urlAuthMap = urlAuthIdMapCommonResult.getResult();
        String uri =request.getURI().getPath();
        int index = uri.indexOf("-api/",1);
        if(index >= 0){
            uri = "/api"+ uri.substring(index+4);
        }
        Integer linkAuth = urlAuthMap.get(uri);
        if(null == linkAuth){
            return ResponseUtil.commonResultResponse(response, CommonResult.errorReturn(CommonCode.NO_PAGE));
        }

        switch (linkAuth){
            case 1:{
                break;
            }case 2:{
                break;
            }case 3:{
                return chain.filter(exchange);
            }case 4:{
                CommonResult<UserInfo> userInfoCommonResult = sysUserService.login(request.getQueryParams().getFirst("username"), request.getQueryParams().getFirst("password"));
                if(userInfoCommonResult.isSuccess()){
                    UserInfo userInfo = userInfoCommonResult.getResult();
                    long tokenResp = idGenerateable.getUniqueID();
                    tokenCache.put(tokenResp, new TokenInfo(userInfo.getUserId(), System.currentTimeMillis()+ 1000L*60*60*24));
                    userInfoCache.put(userInfo.getUserId(), userInfo);
                    response.getCookies().add(AUTHORIZE_TOKEN, ResponseCookie.from(AUTHORIZE_TOKEN
                            , String.valueOf(tokenResp))
                            .path("/")
                            .build());
                    response.getCookies().add(AUTHORIZE_UID, ResponseCookie.from(AUTHORIZE_UID
                            , String.valueOf(userInfo.getUserId()))
                            .path("/")
                            .build());
                }
                return ResponseUtil.commonResultResponse(response, userInfoCommonResult);
            }case 5:{
                break;
            }case 6:{
                break;
            }default:{
                return ResponseUtil.commonResultResponse(response, CommonResult.errorReturn(CommonCode.NO_PAGE));
            }
        }
        MultiValueMap<String, HttpCookie> headers = request.getCookies();
        HttpCookie tokenCookie = headers.getFirst(AUTHORIZE_TOKEN);
        HttpCookie userIdCookie = headers.getFirst(AUTHORIZE_UID);
        if (StringUtils.isEmpty(tokenCookie) || StringUtils.isEmpty(userIdCookie)) {
            return ResponseUtil.commonResultResponse(response, CommonResult.errorReturn(CommonCode.NO_LOGIN_TOKEN));
        }

        Long userId ;
        Long tokenId ;
        try {
            userId = Long.valueOf(userIdCookie.getValue());
            tokenId = Long.valueOf(tokenCookie.getValue());
        }catch (Exception e){
            return ResponseUtil.commonResultResponse(response, CommonResult.errorReturn(CommonCode.LOGIN_ERROR));
        }
        if(5 == linkAuth){
            userInfoCache.remove(tokenId);
            tokenCache.remove(userId);
            return ResponseUtil.commonResultResponse(response, CommonResult.successReturn());
        }
        TokenInfo tokenInfo = tokenCache.get(tokenId);
        if(null == tokenInfo){
            return ResponseUtil.commonResultResponse(response, CommonResult.errorReturn(CommonCode.NO_LOGIN_TOKEN));
        }
        if(null != tokenInfo && System.currentTimeMillis()> tokenInfo.getExpire()){
            return ResponseUtil.commonResultResponse(response, CommonResult.errorReturn(CommonCode.LOGIN_EXPIRE));
        }
        if(!userId.equals(tokenInfo.getUserId())){
            return ResponseUtil.commonResultResponse(response, CommonResult.errorReturn(CommonCode.LOGIN_ERROR));
        }
        UserInfo userInfo = userInfoCache.get(userId);
        if(null == userInfo){
            return ResponseUtil.commonResultResponse(response, CommonResult.errorReturn(CommonCode.NO_LOGIN_USERINFO));
        }
        if(6 == linkAuth){
            return ResponseUtil.commonResultResponse(response, CommonResult.successReturn(userInfo));
        }

        if(1 == linkAuth){
            if(!userInfo.getAuthUrls().contains(uri)){
                return ResponseUtil.commonResultResponse(response, CommonResult.errorReturn(CommonCode.NO_AUTH));
            }
        }
        long traceId = idGenerateable.getUniqueID();
        ServerHttpRequest newHeaders= request.mutate().headers(httpHeaders -> {
            httpHeaders.set(StringDef.TRACE_ID, String.valueOf(traceId));
            httpHeaders.set(StringDef.USER_ID, String.valueOf(userId));
            if(null != userInfo.getTenantId()){
                httpHeaders.set(StringDef.TENANT_ID, String.valueOf(userInfo.getTenantId()));
            }
        }).build();
        return chain.filter(exchange.mutate().request(newHeaders).build());

    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}
