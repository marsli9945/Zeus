package com.tuyoo.framework.grow.admin.service.Imp;

import com.tuyoo.framework.grow.admin.entities.RoleEntities;
import com.tuyoo.framework.grow.admin.entities.UserEntities;
import com.tuyoo.framework.grow.admin.form.user.CreateUserForm;
import com.tuyoo.framework.grow.admin.form.user.EditUserForm;
import com.tuyoo.framework.grow.admin.repository.UserRepository;
import com.tuyoo.framework.grow.admin.service.UserService;
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
public class UserServiceImp implements UserService
{
    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<UserEntities> fetch(Integer page, Integer size, String name)
    {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");

        Specification<UserEntities> specification = (Specification<UserEntities>) (root, criteriaQuery, cb) ->
        {
            List<Predicate> predicates = new ArrayList<>();

            if (!StringUtils.isEmpty(name))
            {
                predicates.add(cb.like(root.get("username").as(String.class), "%" + name + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return userRepository.findAll(specification, pageable);
    }

    @Override
    public UserEntities findByUsernameAndStatusAndRoleEntitiesList(String username, Integer status, RoleEntities roleEntities)
    {
        return userRepository.findByUsernameAndStatusAndRoleEntitiesList(username, status, roleEntities);
    }

    @Override
    public boolean create(CreateUserForm createUserForm)
    {
        UserEntities user = userRepository.findByUsername(createUserForm.getUsername());
        if (user != null)
        {
            return false;
        }

        userRepository.save(createUserForm.entities(new UserEntities()));
        return true;
    }

    @Override
    public boolean update(EditUserForm editUserForm)
    {
        UserEntities user = userRepository.findByUsername(editUserForm.getUsername());
        if (user == null)
        {
            return false;
        }

        UserEntities userEntities = editUserForm.entities(user);
        userEntities.setId(user.getId());
        userRepository.save(userEntities);
        return true;
    }

    @Override
    public boolean delete(String username)
    {
        if (username == null)
        {
            return false;
        }
        UserEntities user = userRepository.findByUsername(username);
        if (user == null)
        {
            return false;
        }

        userRepository.deleteById(user.getId());
        return true;
    }
}
