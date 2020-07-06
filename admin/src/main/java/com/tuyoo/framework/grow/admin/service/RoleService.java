package com.tuyoo.framework.grow.admin.service;

import com.tuyoo.framework.grow.admin.entities.RoleEntities;
import com.tuyoo.framework.grow.admin.form.role.CreateRoleForm;
import com.tuyoo.framework.grow.admin.form.role.EditRoleForm;
import org.springframework.data.domain.Page;

public interface RoleService
{
    Page<RoleEntities> fetch(Integer page, Integer size, String name);

    boolean create(CreateRoleForm createRoleForm);

    boolean update(EditRoleForm editRoleForm);

    boolean delete(Integer id);
}
