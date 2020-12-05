package com.imooc.coupon.service;

import com.imooc.coupon.constants.RoleEnum;
import com.imooc.coupon.dao.PathRepository;
import com.imooc.coupon.dao.RolePathMappingRepository;
import com.imooc.coupon.dao.RoleRepository;
import com.imooc.coupon.dao.UserRoleMappingRepository;
import com.imooc.coupon.entity.Path;
import com.imooc.coupon.entity.Role;
import com.imooc.coupon.entity.RolePathMapping;
import com.imooc.coupon.entity.UserRoleMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class PermissionService {

    @Autowired
    private PathRepository pathRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleMappingRepository userRoleMappingRepository;

    @Autowired
    private RolePathMappingRepository rolePathMappingRepository;


    // 用户访问接口权限校验
    public Boolean checkPermission(Long userId, String uri, String httpMethod) {
        UserRoleMapping userRoleMapping = userRoleMappingRepository.findByUserId(userId);

        if (null == userRoleMapping) {
            log.error("userid not exist is UserRoleMapping: {}", userId);
            return false;
        }

        Optional<Role> role = roleRepository.findById(userRoleMapping.getRoleId());

        // 如果找不到role记录，直接返回false
        if (!role.isPresent()) {
            log.error("roleId not exist in Role: {}", userRoleMapping.getRoleId());
            return false;
        }

        // 如果用户角色是超级管理员，直接返回true
        if (role.get().getRoleTag().equals(RoleEnum.SUPER_ADMIN.name())) {
            return true;
        }

        Path path = pathRepository.findByPathPatternAndHttpMethod(uri, httpMethod);

        if (null == path) {
            return true;
        }

        RolePathMapping rolePathMapping = rolePathMappingRepository.findByRoleIdAndPathId(role.get().getId(),
                path.getId());


        return rolePathMapping != null;
    }
}
