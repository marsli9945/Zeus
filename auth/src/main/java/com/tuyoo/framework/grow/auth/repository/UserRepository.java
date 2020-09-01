package com.tuyoo.framework.grow.auth.repository;

import com.tuyoo.framework.grow.auth.entities.UserEntities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<UserEntities, Integer>, JpaSpecificationExecutor<UserEntities>
{
    UserEntities findByUsername(String username);
}
