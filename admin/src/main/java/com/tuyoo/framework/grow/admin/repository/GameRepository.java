package com.tuyoo.framework.grow.admin.repository;

import com.tuyoo.framework.grow.admin.entities.GameEntities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GameRepository extends JpaRepository<GameEntities, Integer>, JpaSpecificationExecutor<GameEntities>
{
    GameEntities findByProjectId(String projectId);
}
