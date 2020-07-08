package com.tuyoo.framework.grow.admin.repository;

import com.tuyoo.framework.grow.admin.entities.MapEntities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MapRepository extends JpaRepository<MapEntities, Integer>, JpaSpecificationExecutor<MapEntities>
{
    MapEntities findByTypeAndStatus(String type, Integer status);
    MapEntities findByTypeAndStatusAndValue(String type, Integer status, String value);
}
