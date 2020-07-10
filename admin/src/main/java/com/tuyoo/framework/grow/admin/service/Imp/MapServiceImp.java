package com.tuyoo.framework.grow.admin.service.Imp;

import com.tuyoo.framework.grow.admin.entities.MapEntities;
import com.tuyoo.framework.grow.admin.form.map.CreateMapForm;
import com.tuyoo.framework.grow.admin.form.map.EditMapForm;
import com.tuyoo.framework.grow.admin.repository.GameRepository;
import com.tuyoo.framework.grow.admin.repository.MapRepository;
import com.tuyoo.framework.grow.admin.service.MapService;
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
public class MapServiceImp implements MapService
{
    @Autowired
    MapRepository mapRepository;


    @Override
    public Page<MapEntities> fetch(Integer page, Integer size, String name)
    {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");

        Specification<MapEntities> specification = (Specification<MapEntities>) (root, criteriaQuery, cb) ->
        {
            List<Predicate> predicates = new ArrayList<>();

            if (!StringUtils.isEmpty(name))
            {
                predicates.add(cb.like(root.get("label").as(String.class), "%" + name + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return mapRepository.findAll(specification, pageable);
    }

    @Override
    public boolean create(CreateMapForm createMapForm)
    {
        if (mapRepository.findByTypeAndValue(createMapForm.getType(), createMapForm.getValue()) != null)
        {
            return false;
        }
        mapRepository.save(createMapForm.entities(new MapEntities()));
        return true;
    }

    @Override
    public boolean update(EditMapForm editMapForm)
    {

        MapEntities map = mapRepository.findById(editMapForm.getId()).get();
        if (map.getId() == null || mapRepository.findByTypeAndValue(editMapForm.getType(), editMapForm.getValue()) != null)
        {
            return false;
        }
        mapRepository.save(editMapForm.entities(map));
        return true;
    }

    @Override
    public boolean delete(Integer id)
    {
        if (id == null)
        {
            return false;
        }
        if (mapRepository.findById(id).get().getId() == null)
        {
            return false;
        }
        mapRepository.deleteById(id);
        return true;
    }

    @Override
    public List<MapEntities> select(String type)
    {
        return mapRepository.findAllByTypeAndStatus(type,1);
    }
}
