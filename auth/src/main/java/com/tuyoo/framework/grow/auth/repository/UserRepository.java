package com.tuyoo.framework.grow.auth.repository;

import com.tuyoo.framework.grow.auth.entities.UserEntities;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntities, Integer>
{

}
