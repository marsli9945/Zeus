package com.tuyoo.framework.grow.admin.service.Imp;

import com.tuyoo.framework.grow.admin.entities.UserEntities;
import com.tuyoo.framework.grow.admin.ga.GaConfig;
import com.tuyoo.framework.grow.admin.ga.form.GaUserForm;
import com.tuyoo.framework.grow.admin.repository.UserRepository;
import com.tuyoo.framework.grow.admin.service.GaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class GaUserServiceImp implements GaUserService
{
    @Autowired
    GaConfig gaConfig;

    @Autowired
    UserRepository userRepository;

    @Override
    public Page<UserEntities> fetch(Integer page, Integer size, String name)
    {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");

        Specification<UserEntities> specification = (Specification<UserEntities>) (root, criteriaQuery, cb) ->
        {
            List<Predicate> predicates = new ArrayList<>();
            Join<Object, Object> role = root.join("roleEntitiesList", JoinType.INNER);

            predicates.add(cb.equal(role.get("id"), gaConfig.getRoleId()));

            if (!StringUtils.isEmpty(name))
            {
                predicates.add(cb.like(root.get("username"), "%" + name + "%"));
            }


            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return userRepository.findAll(specification, pageable);
    }

    @Override
    public boolean create(GaUserForm gaUserForm)
    {
        return false;
    }

    @Override
    public boolean update(GaUserForm gaUserForm)
    {
        return false;
    }

    @Override
    public boolean delete(String username)
    {
        return false;
    }
}
