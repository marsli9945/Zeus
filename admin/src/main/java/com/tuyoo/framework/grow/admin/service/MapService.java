package com.tuyoo.framework.grow.admin.service;

import com.tuyoo.framework.grow.admin.entities.MapEntities;
import com.tuyoo.framework.grow.admin.form.map.CreateMapForm;
import com.tuyoo.framework.grow.admin.form.map.EditMapForm;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MapService
{
    Page<MapEntities> fetch(Integer page, Integer size, String name);

    boolean create(CreateMapForm createMapForm);

    boolean update(EditMapForm editMapForm);

    boolean delete(Integer id);

    List<MapEntities> select(String type);
}
