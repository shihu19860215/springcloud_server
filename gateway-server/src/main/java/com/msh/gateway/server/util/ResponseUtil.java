package com.msh.gateway.server.util;

import com.msh.frame.client.common.CommonResult;
import com.msh.frame.common.util.StringUtil;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

public class ResponseUtil {
    private static final String ALLOWED_HEADERS = "x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN,token,username,client";
    private static final String ALLOWED_METHODS = StringUtil.ASTERISK;
    private static final String ALLOWED_ORIGIN = "http://localhost:8080";
    private static final String ALLOWED_EXPOSE = StringUtil.ASTERISK;
    private static final String MAX_AGE = "3600";

    public static Mono<Void> commonResultResponse(ServerHttpResponse response, CommonResult commonResult){
        HttpHeaders httpHeaders = response.getHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
        headersAddOrigin(httpHeaders);
        response.setStatusCode(HttpStatus.OK);
        DataBuffer dataBuffer = response.bufferFactory().wrap(commonResult.toJsonString().getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Flux.just(dataBuffer));
    }

    public static void headersAddOrigin(HttpHeaders headers){
        headers.set("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
        headers.add("Access-sControl-Allow-Methods", ALLOWED_METHODS);
        headers.add("Access-Control-Max-Age", MAX_AGE);
        headers.add("Access-Control-Allow-Headers", ALLOWED_HEADERS);
        headers.add("Access-Control-Expose-Headers", ALLOWED_EXPOSE);
        headers.add("Access-Control-Allow-Credentials", "true");
    }
}
