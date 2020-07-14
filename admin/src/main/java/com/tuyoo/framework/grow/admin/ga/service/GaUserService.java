package com.tuyoo.framework.grow.admin.ga.service;

import com.tuyoo.framework.grow.admin.entities.UserEntities;
import com.tuyoo.framework.grow.admin.ga.entities.GaSelectEntities;
import com.tuyoo.framework.grow.admin.ga.entities.GaStudioEntities;
import com.tuyoo.framework.grow.admin.ga.entities.GaUserInfoEntities;
import com.tuyoo.framework.grow.admin.ga.form.GaUserForm;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GaUserService
{
    Page<UserEntities> fetch(Integer page, Integer size, String name);

    GaUserInfoEntities userInfo();

    List<GaStudioEntities> getPermission(String username);

    List<GaSelectEntities> getStudioSelect();

    boolean create(GaUserForm gaUserForm);

    boolean update(GaUserForm gaUserForm);

    boolean delete(String username);

    void clearNoPermissionUser(String username);

    void addAutoPermissionGame(Integer studioId, Integer gameId);
}
