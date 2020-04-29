package com.tuyoo.framework.grow.auth.repository;

import com.tuyoo.framework.grow.auth.entities.ClientEntities;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<ClientEntities, Integer>
{
    ClientEntities findByClientId(String clientId);
}
