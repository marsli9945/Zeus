package com.tuyoo.framework.grow.admin.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Table
@Entity(name = "admin_studio")
public class StudioEntities
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    private String admin;

    @Column
    private Integer status = 1;

    @OneToMany(mappedBy = "studio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GameEntities> gameEntities;
}
