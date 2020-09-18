package com.tuyoo.framework.grow.admin.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Table
@NoArgsConstructor
@AllArgsConstructor
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

    @Column
    private String region;

    @Column
    private String type;

    @JsonBackReference
    @ManyToOne(cascade={CascadeType.DETACH,CascadeType.REFRESH},optional=false)
    private StudioEntities studio;
}
