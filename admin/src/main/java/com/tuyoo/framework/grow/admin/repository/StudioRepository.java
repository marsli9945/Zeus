package com.tuyoo.framework.grow.admin.repository;

import com.tuyoo.framework.grow.admin.entities.StudioEntities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StudioRepository extends JpaRepository<StudioEntities, Integer>, JpaSpecificationExecutor<StudioEntities>
{
    StudioEntities findByName(String name);
}
