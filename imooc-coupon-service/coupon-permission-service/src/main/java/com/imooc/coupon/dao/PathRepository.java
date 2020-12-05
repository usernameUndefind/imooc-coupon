package com.imooc.coupon.dao;


import com.imooc.coupon.entity.Path;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PathRepository extends JpaRepository<Path, Integer> {

    // 根据服务名称查找path记录
    List<Path> findAllByServiceName(String serviceName);


    // 根据路径模式 + 请求类型查找数据记录
    Path findByPathPatternAndHttpMethod(String pathPattern, String httpMethod);


}
