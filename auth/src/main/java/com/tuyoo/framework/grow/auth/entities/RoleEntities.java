package com.tuyoo.framework.grow.auth.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "springcloud_role")
public class RoleEntities
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @ManyToMany
    private List<UserEntities> userEntitiesList;

    public List<UserEntities> getUserEntitiesList()
    {
        return userEntitiesList;
    }

    public void setUserEntitiesList(List<UserEntities> userEntitiesList)
    {
        this.userEntitiesList = userEntitiesList;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
