package com.tuyoo.framework.grow.auth.repository;

import com.tuyoo.framework.grow.auth.entities.UserPermissionEntities;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPermissionRepository extends JpaRepository<UserPermissionEntities, Integer>
{
    List<UserPermissionEntities> findAllByUsername(String username);
}
