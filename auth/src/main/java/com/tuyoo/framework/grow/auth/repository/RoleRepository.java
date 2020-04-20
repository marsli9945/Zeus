package com.tuyoo.framework.grow.auth.repository;

import com.tuyoo.framework.grow.auth.entities.RoleEntities;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntities, Integer>
{

}
