package com.tuyoo.framework.grow.admin.service.Imp;

import com.tuyoo.framework.grow.admin.entities.ClientEntities;
import com.tuyoo.framework.grow.admin.form.client.CreateClientForm;
import com.tuyoo.framework.grow.admin.form.client.EditClientForm;
import com.tuyoo.framework.grow.admin.repository.ClientRepository;
import com.tuyoo.framework.grow.admin.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClientServiceImp implements ClientService
{
    @Autowired
    ClientRepository clientRepository;

    @Override
    public Page<ClientEntities> fetch(Integer page, Integer size, String clientId)
    {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");

        Specification<ClientEntities> specification = (Specification<ClientEntities>) (root, criteriaQuery, cb) ->
        {
            List<Predicate> predicates = new ArrayList<>();

            if (!StringUtils.isEmpty(clientId))
            {
                predicates.add(cb.like(root.get("clientId").as(String.class), "%" + clientId + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return clientRepository.findAll(specification, pageable);
    }

    @Override
    public boolean create(CreateClientForm createClientForm)
    {
        if (clientRepository.findByClientId(createClientForm.getClientId()) != null)
        {
            return false;
        }
        clientRepository.save(createClientForm.entities(new ClientEntities()));
        return true;
    }

    @Override
    public boolean update(EditClientForm editClientForm)
    {
        ClientEntities client = clientRepository.findByClientId(editClientForm.getClientId());
        if (client == null)
        {
            return false;
        }
        clientRepository.save(editClientForm.entities(client));
        return true;
    }

    @Override
    public boolean delete(String clientId)
    {
        if (clientId == null)
        {
            return false;
        }
        ClientEntities client = clientRepository.findByClientId(clientId);
        if (client == null)
        {
            return false;
        }
        clientRepository.deleteById(client.getId());
        return true;
    }
}
