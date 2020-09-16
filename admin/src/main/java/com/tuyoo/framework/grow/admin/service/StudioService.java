package com.tuyoo.framework.grow.admin.service;

import com.tuyoo.framework.grow.admin.entities.StudioEntities;
import com.tuyoo.framework.grow.admin.entities.StudioOutEntities;
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

    List<StudioOutEntities> fetchAll();
}
