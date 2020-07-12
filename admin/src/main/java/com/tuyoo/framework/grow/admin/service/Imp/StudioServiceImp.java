package com.tuyoo.framework.grow.admin.service.Imp;

import com.tuyoo.framework.grow.admin.entities.StudioEntities;
import com.tuyoo.framework.grow.admin.form.studio.CreateStudioForm;
import com.tuyoo.framework.grow.admin.form.studio.EditStudioForm;
import com.tuyoo.framework.grow.admin.repository.PermissionRepository;
import com.tuyoo.framework.grow.admin.repository.StudioRepository;
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
import java.util.List;

@Service
public class StudioServiceImp implements StudioService
{
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
    public boolean isStudioAdmin(String username, Integer studioId)
    {
        return studioRepository.findByAdminAndIdAndStatus(username, studioId, 1) != null;
    }

    @Override
    public List<StudioEntities> findAllIsAdminStudio(String username)
    {
        return studioRepository.findAllByAdminAndStatus(username, 1);
    }
}
