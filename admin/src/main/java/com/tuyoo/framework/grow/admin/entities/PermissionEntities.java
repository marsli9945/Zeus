package com.tuyoo.framework.grow.admin.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Table
@Entity(name = "admin_user_permission")
public class PermissionEntities
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String username;

    @Column
    private Integer studioId;

    @Column
    private String game;

    @Column
    private String permission;

    @Column
    private Integer isAuto = 0;

    @Column
    private Integer isDistribute = 0;

    @Column
    private Integer isAccessGame = 0;
}
