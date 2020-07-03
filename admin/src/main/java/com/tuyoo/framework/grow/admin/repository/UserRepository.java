package com.tuyoo.framework.grow.admin.repository;

import com.tuyoo.framework.grow.admin.entities.UserEntities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntities, Integer>, JpaSpecificationExecutor
{
    UserEntities findByUsername(String username);
}
