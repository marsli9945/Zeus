package com.tuyoo.framework.grow.admin.service;

import com.tuyoo.framework.grow.admin.entities.ClientEntities;
import com.tuyoo.framework.grow.admin.form.client.CreateClientForm;
import com.tuyoo.framework.grow.admin.form.client.EditClientForm;
import org.springframework.data.domain.Page;

public interface ClientService
{
    Page<ClientEntities> fetch(Integer page, Integer size, String name);

    boolean create(CreateClientForm createClientForm);

    boolean update(EditClientForm editClientForm);

    boolean delete(String clientId);
}
