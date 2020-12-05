package com.imooc.coupon.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "coupon_role_path_mapping")
public class RolePathMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    /**
     * Role 表的主键
     */
    @Basic
    @Column(name = "role_id", nullable = false)
    private Integer roleId;

    /**
     * path表的主键
     */
    @Basic
    @Column(name = "path_id", nullable = false)
    private Integer pathId;

}
