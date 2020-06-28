package com.tuyoo.framework.grow.admin.service;

import com.tuyoo.framework.grow.admin.entities.UserEntities;

import java.util.List;

public interface UserService
{
    UserEntities findOne(Long id);

    UserEntities save(UserEntities user);

    List<UserEntities> queryAll();
}
