package com.tuyoo.framework.grow.admin.repository;

import com.tuyoo.framework.grow.admin.entities.UserEntities;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<UserEntities, Long>
{
}
