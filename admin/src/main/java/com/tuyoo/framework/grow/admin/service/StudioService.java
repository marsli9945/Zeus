package com.tuyoo.framework.grow.admin.service;

import com.tuyoo.framework.grow.admin.entities.StudioEntities;
import com.tuyoo.framework.grow.admin.form.studio.CreateStudioForm;
import com.tuyoo.framework.grow.admin.form.studio.EditStudioForm;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StudioService
{
    Page<StudioEntities> fetch(Integer page, Integer size, String name);

    boolean create(CreateStudioForm createStudioForm);

    boolean update(EditStudioForm editStudioForm);

    boolean delete(Integer id);

    /**
     * 判断用户是否为该工作室管理员
     * @param username 需查询用户
     * @param studioId 工作室ID
     * @return true/false
     */
    boolean isStudioAdmin(String username, Integer studioId);

    /**
     * 获取用户是管理员的所有工作室
     * @param username 用户名
     * @return 工作室列表
     */
    List<StudioEntities> findAllIsAdminStudio(String username);
}
