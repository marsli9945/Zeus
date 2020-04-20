package com.tuyoo.framework.grow.auth.entities;

import javax.persistence.*;

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
