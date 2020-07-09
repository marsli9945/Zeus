package com.tuyoo.framework.grow.admin.repository;

import com.tuyoo.framework.grow.admin.entities.PermissionEntities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PermissionRepository extends JpaRepository<PermissionEntities, Integer>, JpaSpecificationExecutor<PermissionRepository>
{
}
