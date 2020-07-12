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
    List<PermissionEntities> findAllByUsernameAndIsDistribute(String username, Integer isDistribute);
    List<PermissionEntities> findAllByStudioIdIn(List<Integer> studioList);
    List<PermissionEntities> findAllByUsernameAndStudioIdNotIn(String username, List<Integer> studioIdList);
    List<PermissionEntities> findAllByUsername(String username);
    void deleteAllByUsername(String username);
    void deleteAllByUsernameAndStudioIdIn(String username, List<Integer> studioIdList);
}
