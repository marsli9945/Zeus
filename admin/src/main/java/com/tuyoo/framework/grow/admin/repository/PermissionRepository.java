package com.tuyoo.framework.grow.admin.repository;

import com.tuyoo.framework.grow.admin.entities.PermissionEntities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntities, Integer>, JpaSpecificationExecutor<PermissionRepository>
{
    PermissionEntities findByUsernameAndStudioId(String username, Integer studioId);
    List<PermissionEntities> findAllByUsernameAndIsDistributeAndStatus(String username, Integer isDistribute, Integer status);
    List<PermissionEntities> findAllByStudioIdInAndStatus(List<Integer> studioList, Integer status);
    List<PermissionEntities> findAllByUsernameAndStudioIdNotInAndStatus(String username, List<Integer> studioIdList, Integer status);
    List<PermissionEntities> findAllByUsernameAndStatus(String username, Integer status);
}
