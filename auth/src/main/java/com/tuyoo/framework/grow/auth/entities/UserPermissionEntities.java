package com.tuyoo.framework.grow.auth.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Table
@Entity(name = "admin_user_permission")
public class UserPermissionEntities
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增主键
    private Integer id;

    @Column
    private String username;

    @Column
    private String game;
}
