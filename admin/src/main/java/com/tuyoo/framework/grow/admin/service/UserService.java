package com.tuyoo.framework.grow.admin.service;

import com.tuyoo.framework.grow.admin.entities.RoleEntities;
import com.tuyoo.framework.grow.admin.entities.UserEntities;
import com.tuyoo.framework.grow.admin.form.user.CreateUserForm;
import com.tuyoo.framework.grow.admin.form.user.EditUserForm;
import org.springframework.data.domain.Page;

public interface UserService
{
    Page<UserEntities> fetch(Integer page, Integer size, String name);

    UserEntities findByUsernameAndStatusAndRoleEntitiesList(String username, Integer status, RoleEntities roleEntities);

    boolean create(CreateUserForm createUserForm);

    boolean update(EditUserForm editUserForm);

    boolean delete(String username);
}
