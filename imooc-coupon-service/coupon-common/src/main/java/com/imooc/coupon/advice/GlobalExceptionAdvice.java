package com.imooc.coupon.advice;

import com.imooc.coupon.exception.CouponException;
import com.imooc.coupon.vo.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionAdvice {


    @ExceptionHandler(value = Exception.class)
    public CommonResponse<String> handlerCouponException(
            HttpServletRequest request, Exception ex
    ) {

        log.error("抓捕到异常 {}", ex.getStackTrace());
        CommonResponse<String> response = new CommonResponse<>(
                -1, "business error"
        );

        ex.printStackTrace();

        response.setData(ex.getMessage());
        return response;
    }
}
