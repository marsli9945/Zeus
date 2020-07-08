package com.tuyoo.framework.grow.admin.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Table
@Entity(name = "admin_game")
public class GameEntities
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String projectId;

    @Column
    private String name;

    @Column
    private String icon;

    @Column
    private String timeZone;

    @Column
    private String currency;

    @Column
    private Integer status = 1;

    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH},optional=false)
    private StudioEntities studio;
}
