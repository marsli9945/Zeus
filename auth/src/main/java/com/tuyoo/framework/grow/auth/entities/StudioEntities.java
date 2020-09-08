package com.tuyoo.framework.grow.auth.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Table
@AllArgsConstructor
@NoArgsConstructor
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

    @OneToMany(mappedBy = "studio", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<GameEntities> gameEntities;
}
