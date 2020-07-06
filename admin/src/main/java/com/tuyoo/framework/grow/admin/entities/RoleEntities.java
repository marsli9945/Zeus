package com.tuyoo.framework.grow.admin.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Table
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "admin_role")
public class RoleEntities
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;
}
