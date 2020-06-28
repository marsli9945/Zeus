package com.tuyoo.framework.grow.admin.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")
@Data
@AllArgsConstructor
@ToString
public class UserEntities
{
    @Id
    private Long id;

    private String username;

    private String password;

    private int age;
}
