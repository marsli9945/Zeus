package com.tuyoo.framework.grow.security.respository;

import com.tuyoo.framework.grow.security.entities.UserEntities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntities, Integer>
{
    UserEntities findUserEntitiesByUsername(String username);
}
