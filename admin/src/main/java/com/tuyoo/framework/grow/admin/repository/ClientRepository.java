package com.tuyoo.framework.grow.admin.repository;

import com.tuyoo.framework.grow.admin.entities.ClientEntities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ClientRepository extends JpaRepository<ClientEntities, Integer>, JpaSpecificationExecutor<ClientEntities>
{
    ClientEntities findByClientId(String clientId);
}
