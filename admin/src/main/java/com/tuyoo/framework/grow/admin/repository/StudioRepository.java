package com.tuyoo.framework.grow.admin.repository;

import com.tuyoo.framework.grow.admin.entities.StudioEntities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudioRepository extends JpaRepository<StudioEntities, Integer>, JpaSpecificationExecutor<StudioEntities>
{
    StudioEntities findByName(String name);
    List<StudioEntities> findAllByStatus(Integer status);
    List<StudioEntities> findAllByAdminAndStatus(String username, Integer status);
    List<StudioEntities> findAllByIdInAndStatus(List<Integer> idList, Integer status);
    StudioEntities findByAdminAndIdAndStatus(String username, Integer id, Integer status);
}
