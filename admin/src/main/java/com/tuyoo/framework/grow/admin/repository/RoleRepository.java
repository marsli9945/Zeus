package com.tuyoo.framework.grow.admin.repository;

import com.tuyoo.framework.grow.admin.entities.RoleEntities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntities, Integer>, JpaSpecificationExecutor<RoleEntities>
{
    RoleEntities findByName(String name);
}
