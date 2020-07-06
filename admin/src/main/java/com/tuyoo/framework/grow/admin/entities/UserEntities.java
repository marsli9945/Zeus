package com.tuyoo.framework.grow.admin.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.*;
import java.util.List;

@Data
@Table
@Entity(name = "admin_user")
public class UserEntities
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String name;

    @Column
    private int level;

    @Column
    private Integer status = 0;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "admin_user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<RoleEntities> roleEntitiesList;
}
