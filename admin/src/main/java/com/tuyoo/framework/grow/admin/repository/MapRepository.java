package com.tuyoo.framework.grow.admin.repository;

import com.tuyoo.framework.grow.admin.entities.MapEntities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MapRepository extends JpaRepository<MapEntities, Integer>, JpaSpecificationExecutor<MapEntities>
{
    MapEntities findByTypeAndValue(String type, String value);
    MapEntities findByTypeAndValueAndIdNot(String type, String value, Integer id);
    List<MapEntities> findAllByTypeAndStatus(String type, Integer status);
}
