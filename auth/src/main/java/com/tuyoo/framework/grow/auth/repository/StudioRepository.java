package com.tuyoo.framework.grow.auth.repository;

import com.tuyoo.framework.grow.auth.entities.StudioEntities;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudioRepository extends JpaRepository<StudioEntities, Integer>
{
    List<StudioEntities> findAllByAdmin(String admin);
}
