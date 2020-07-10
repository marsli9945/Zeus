package com.tuyoo.framework.grow.admin.service.Imp;

import com.tuyoo.framework.grow.admin.entities.RoleEntities;
import com.tuyoo.framework.grow.admin.form.role.CreateRoleForm;
import com.tuyoo.framework.grow.admin.form.role.EditRoleForm;
import com.tuyoo.framework.grow.admin.repository.RoleRepository;
import com.tuyoo.framework.grow.admin.service.RoleService;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
public class RoleServiceImp implements RoleService
{
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Page<RoleEntities> fetch(Integer page, Integer size, String name)
    {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");

        Specification<RoleEntities> specification = (Specification<RoleEntities>) (root, criteriaQuery, cb) ->
        {
            List<Predicate> predicates = new ArrayList<>();

            if (!StringUtils.isEmpty(name))
            {
                predicates.add(cb.like(root.get("name").as(String.class), "%" + name + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return roleRepository.findAll(specification, pageable);
    }

    @Override
    public boolean create(CreateRoleForm createRoleForm)
    {
        if (roleRepository.findByName(createRoleForm.getName()) != null) {
            return false;
        }
        roleRepository.save(createRoleForm.entities());
        return true;
    }

    @Override
    public boolean update(EditRoleForm editRoleForm)
    {
        RoleEntities roleEntities = roleRepository.findById(editRoleForm.getId()).orElse(null);
        if (roleEntities == null || roleRepository.findByName(editRoleForm.getName()) != null) {
            return false;
        }
        roleRepository.save(editRoleForm.entities());
        return true;
    }

    @Override
    public boolean delete(Integer id)
    {
        if (id == null)
        {
            return false;
        }
        if (roleRepository.findById(id).orElse(null) == null) {
            return false;
        }
        roleRepository.deleteById(id);
        return true;
    }
}
