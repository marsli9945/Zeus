package com.tuyoo.framework.grow.admin.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Table
@Entity(name = "admin_map")
public class MapEntities
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String type;

    @Column
    private String value;

    @Column
    private String label;

    @Column
    private Integer status = 1;
}
