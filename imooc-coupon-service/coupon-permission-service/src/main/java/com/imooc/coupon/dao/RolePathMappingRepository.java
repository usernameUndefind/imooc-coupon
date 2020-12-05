package com.imooc.coupon.dao;

import com.imooc.coupon.entity.RolePathMapping;
import org.omg.CORBA.INTERNAL;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolePathMappingRepository extends JpaRepository<RolePathMapping, Integer> {

    RolePathMapping findByRoleIdAndPathId(Integer roleId, Integer pathId);
}
