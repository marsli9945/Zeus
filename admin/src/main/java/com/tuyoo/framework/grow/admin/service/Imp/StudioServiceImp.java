package com.tuyoo.framework.grow.admin.service.Imp;

import com.tuyoo.framework.grow.admin.entities.*;
import com.tuyoo.framework.grow.admin.form.studio.CreateStudioForm;
import com.tuyoo.framework.grow.admin.form.studio.EditStudioForm;
import com.tuyoo.framework.grow.admin.ga.GaConfig;
import com.tuyoo.framework.grow.admin.repository.PermissionRepository;
import com.tuyoo.framework.grow.admin.repository.StudioRepository;
import com.tuyoo.framework.grow.admin.repository.UserRepository;
import com.tuyoo.framework.grow.admin.service.StudioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class StudioServiceImp implements StudioService
{
    @Autowired
    GaConfig gaConfig;

    @Autowired
    UserRepository userRepository;

    @Autowired
    StudioRepository studioRepository;

    @Autowired
    PermissionRepository permissionRepository;

    @Override
    public Page<StudioEntities> fetch(Integer page, Integer size, String name)
    {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");

        Specification<StudioEntities> specification = (Specification<StudioEntities>) (root, criteriaQuery, cb) ->
        {
            List<Predicate> predicates = new ArrayList<>();

            if (!StringUtils.isEmpty(name))
            {
                predicates.add(cb.like(root.get("name").as(String.class), "%" + name + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return studioRepository.findAll(specification, pageable);
    }

    @Override
    public boolean create(CreateStudioForm createStudioForm)
    {
        if (studioRepository.findByName(createStudioForm.getName()) != null)
        {
            return false;
        }
        studioRepository.save(createStudioForm.entities(new StudioEntities()));
        return true;
    }

    @Override
    public boolean update(EditStudioForm editStudioForm)
    {
        StudioEntities studio = studioRepository.findById(editStudioForm.getId()).orElse(null);
        if (studio == null)
        {
            return false;
        }
        studioRepository.save(editStudioForm.entities(studio));
        return true;
    }

    @Override
    @Transactional
    public boolean delete(Integer id)
    {
        if (id == null)
        {
            return false;
        }
        if (studioRepository.findById(id).orElse(null) == null)
        {
            return false;
        }
        studioRepository.deleteById(id);
        // 同时删除权限中所有该工作室的记录
        permissionRepository.deleteAllByStudioId(id);
        return true;
    }

    @Override
    public List<StudioOutEntities> fetchAll()
    {
        // 生成用户映射集合
        List<UserEntities> userEntitiesList = userRepository.findAllByStatusAndRoleEntitiesList(1, new RoleEntities(gaConfig.getRoleId(), null));
        HashMap<String, String> userMap = new HashMap<>();
        for (UserEntities userEntities :
                userEntitiesList)
        {
            userMap.put(userEntities.getUsername(), userEntities.getName());
        }

        // 获取工作室拥有增删权限的用户列表
        List<PermissionEntities> permissionEntitiesList = permissionRepository.findAllByIsDistribute(1);
        HashMap<Integer, List<String>> distributeList = new HashMap<>();
        for (PermissionEntities permissionEntities :
                permissionEntitiesList)
        {
            distributeList.computeIfAbsent(permissionEntities.getStudioId(), k -> new ArrayList<>());
            distributeList.get(permissionEntities.getStudioId()).add(userMap.get(permissionEntities.getUsername()));
        }

        // 装配输出的工作室实体
        ArrayList<StudioOutEntities> all = new ArrayList<>();
        List<StudioEntities> studioEntitiesList = studioRepository.findAll();
        for (StudioEntities studioEntities :
                studioEntitiesList)
        {
            StudioOutEntities studioOutEntities = new StudioOutEntities();
            studioOutEntities.setId(studioEntities.getId());
            studioOutEntities.setName(studioEntities.getName());
            studioOutEntities.setGameEntities(studioEntities.getGameEntities());
            studioOutEntities.setAdmin(userMap.get(studioEntities.getAdmin()));
            studioOutEntities.setDistribute(distributeList.get(studioEntities.getId()));
            all.add(studioOutEntities);
        }

        return all;
    }
}
