package com.tuyoo.framework.grow.auth.repository;

import com.tuyoo.framework.grow.auth.entities.GameEntities;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<GameEntities, String>
{
}
