package com.imooc.coupon;

import com.imooc.coupon.vo.PermissionInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 接口权限信息扫描器
 */
@Slf4j
public class AnnotationScanner {

    private String pathPrefix;

    private static final String IMOOC_COUPON_PKG = "com.imooc.coupon";

    /**
     * 狗仔单个接口中的权限信息
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
        return null;
    }
}
