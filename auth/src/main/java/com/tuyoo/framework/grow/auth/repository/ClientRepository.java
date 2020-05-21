package com.tuyoo.framework.grow.auth.repository;

import com.tuyoo.framework.grow.auth.entities.ClientEntities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ClientRepository extends JpaRepository<ClientEntities, Integer>, JpaSpecificationExecutor
{
    ClientEntities findByClientId(String clientId);
}
