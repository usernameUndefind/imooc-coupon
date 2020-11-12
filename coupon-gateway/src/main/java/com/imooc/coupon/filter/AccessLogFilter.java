package com.imooc.coupon.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class AccessLogFilter extends AbstractPostZuulFilter {


    @Override
    protected Object cRun() {

        HttpServletRequest request = context.getRequest();
        // 从 PreRequestFilter 中获取设置的请求时间戳
        Long startTIme = (Long) context.get("startTime");
        String uri = request.getRequestURI();
        long duration = System.currentTimeMillis() - startTIme;

        // 从网关通过的请求都会打印日志记录：uri + duration
        log.info("uri: {}, duration: {}ms", uri, duration);
        return success();
    }

    @Override
    public int filterOrder() {
        // zuul 默认的response组件的filterOrder是1000
        return FilterConstants.SEND_RESPONSE_FILTER_ORDER - 1;
    }
}
