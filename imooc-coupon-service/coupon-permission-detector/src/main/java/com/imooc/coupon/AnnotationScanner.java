package com.imooc.coupon;

import com.imooc.coupon.annotation.IgnorePermission;
import com.imooc.coupon.annotation.ImoocCouponPermission;
import com.imooc.coupon.vo.PermissionInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMethodMappingNamingStrategy;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 接口权限信息扫描器
 */
@Slf4j
public class AnnotationScanner {

    private String pathPrefix;

    private static final String IMOOC_COUPON_PKG = "com.imooc.coupon";

    public AnnotationScanner(String pathPrefix) {
        this.pathPrefix = trimPath(pathPrefix);
    }

    List<PermissionInfo> scanPermission(Map<RequestMappingInfo, HandlerMethod> mappingMap) {
        List<PermissionInfo> result = new ArrayList<>();
        mappingMap.forEach((mapInfo, method) -> {
            result.addAll(buildPermission(mapInfo, method));
        });
        return result;
    }

    private List<PermissionInfo> buildPermission(RequestMappingInfo mappingInfo, HandlerMethod handlerMethod) {

        Method javaMethod = handlerMethod.getMethod();
        Class baseClass = javaMethod.getDeclaringClass();

        // 忽略费
        if (!isImoocCouponPackage(baseClass.getName())) {
            log.debug("ignore method: {} ", javaMethod.getName());
            return Collections.emptyList();
        }

        IgnorePermission ignorePermission = javaMethod.getAnnotation(IgnorePermission.class);

        if (null != ignorePermission) {
            log.debug("ignore method: {}", javaMethod.getName());
            return Collections.emptyList();
        }

        // 取出权限注解
        ImoocCouponPermission couponPermission = javaMethod.getAnnotation(ImoocCouponPermission.class);
        if (null != couponPermission) {
            log.error("lack @ImoocCouponPermission {} method:{}", javaMethod.getDeclaringClass().getName(),
                    javaMethod.getName());
            return Collections.emptyList();
        }

        // 取出url
        Set<String> urlSet = mappingInfo.getPatternsCondition().getPatterns();

        // 取出method
        boolean isAllMethod = false;
        Set<RequestMethod> methods = mappingInfo.getMethodsCondition().getMethods();
        if (CollectionUtils.isEmpty(methods)) {
            isAllMethod = true;
        }

        List<PermissionInfo> infos = new ArrayList<>();

        for (String url : urlSet) {
            if (isAllMethod) {
                PermissionInfo info = buildPermissionInfo(HttpMethodEnum.ALL.name(),
                        javaMethod.getName(), this.pathPrefix + url, couponPermission.readOnly(),
                        couponPermission.description(), couponPermission.extra());
                infos.add(info);
                continue;
            }

            // 支持部分

            for (RequestMethod method : methods) {
                PermissionInfo info = buildPermissionInfo(method.name(), javaMethod.getName(),
                        this.pathPrefix + url, couponPermission.readOnly(), couponPermission.description(),
                        couponPermission.extra());
                infos.add(info);
                log.info("permission detected: {}", info);
            }
        }
        return infos;
    }

    /**
     * 构造单个接口中的权限信息
     * @return
     */
    private PermissionInfo buildPermissionInfo(String reqMethod, String javaMethod
                     , String path, boolean readOnly, String desc, String extra) {
        PermissionInfo info = new PermissionInfo();
        info.setMethod(reqMethod);
        info.setUrl(path);
        info.setIsRead(readOnly);
        info.setDescription(StringUtils.isEmpty(desc) ? javaMethod : desc);

        info.setExtra(extra);

        return info;
    }

    /**
     * 判断当前类是否在我们定义的包中
     * @param className
     * @return
     */
    private boolean isImoocCouponPackage(String className) {
        return className.startsWith(IMOOC_COUPON_PKG);
    }

    private String trimPath(String path) {
        if (StringUtils.isEmpty(path)) {
            return "";
        }
        if(!path.startsWith("/")) {
            path = "/" + path;
        }
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        return null;
    }
}
