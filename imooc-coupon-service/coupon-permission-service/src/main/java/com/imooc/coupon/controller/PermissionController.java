package com.imooc.coupon.controller;

import com.imooc.coupon.annotation.IgnoreResponseAdvise;
import com.imooc.coupon.service.PathService;
import com.imooc.coupon.service.PermissionService;
import com.imooc.coupon.vo.CheckPermissionRequest;
import com.imooc.coupon.vo.CreatePathRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class PermissionController {


    @Autowired
    private PathService pathService;

    @Autowired
    private PermissionService permissionService;

    @PostMapping("/create/path")
    public List<Integer> createPath(@RequestBody CreatePathRequest request) {
        log.info("createPath: {}", request.getPathInfos().size());
        return pathService.createPath(request);
    }

    /**
     * 权限校验接口
     * @param request
     * @return
     */
    @IgnoreResponseAdvise
    @PostMapping("/check/permission")
    public Boolean checkPermission(@RequestBody CheckPermissionRequest request) {
        log.info("checkPermission for args {}, {}, {}", request.getUserId(), request.getUri(), request.getHttpMethod());
        return permissionService.checkPermission(request.getUserId(), request.getUri(), request.getHttpMethod());
    }
}
