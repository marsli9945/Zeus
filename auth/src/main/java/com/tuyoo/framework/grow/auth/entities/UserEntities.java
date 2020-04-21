package com.tuyoo.framework.grow.auth.entities;

import org.springframework.data.repository.cdi.Eager;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "springcloud_user")
public class UserEntities
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增主键
    private Integer id;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String phone;

    @Column
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "springcloud_user_role",joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<RoleEntities> roleEntitiesList;

    public List<RoleEntities> getRoleEntitiesList()
    {
        return roleEntitiesList;
    }

    public void setRoleEntitiesList(List<RoleEntities> roleEntitiesList)
    {
        this.roleEntitiesList = roleEntitiesList;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }
}
