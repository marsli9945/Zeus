package com.tuyoo.framework.grow.auth.entities;


import lombok.Data;

import javax.persistence.*;

@Data
@Table
@Entity(name = "admin_game")
public class GameEntities
{
    @Id
    private String id;

    @Column
    private String projectId;
}
