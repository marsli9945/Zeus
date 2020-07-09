package com.tuyoo.framework.grow.admin.service;

import com.tuyoo.framework.grow.admin.entities.UserEntities;
import com.tuyoo.framework.grow.admin.ga.form.GaUserForm;
import org.springframework.data.domain.Page;

public interface GaUserService
{
    Page<UserEntities> fetch(Integer page, Integer size, String name);

    boolean create(GaUserForm gaUserForm);

    boolean update(GaUserForm gaUserForm);

    boolean delete(String username);
}
