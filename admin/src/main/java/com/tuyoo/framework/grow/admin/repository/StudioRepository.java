package com.tuyoo.framework.grow.admin.repository;

import com.tuyoo.framework.grow.admin.entities.StudioEntities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface StudioRepository extends JpaRepository<StudioEntities, Integer>, JpaSpecificationExecutor<StudioEntities>
{
    StudioEntities findByName(String name);
    List<StudioEntities> findAllByAdmin(String username);
    StudioEntities findByAdminAndId(String username, Integer id);
}
