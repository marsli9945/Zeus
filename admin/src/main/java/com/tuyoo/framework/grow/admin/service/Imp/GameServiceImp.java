package com.tuyoo.framework.grow.admin.service.Imp;

import com.tuyoo.framework.grow.admin.entities.GameEntities;
import com.tuyoo.framework.grow.admin.form.game.CreateGameForm;
import com.tuyoo.framework.grow.admin.form.game.EditGameForm;
import com.tuyoo.framework.grow.admin.repository.GameRepository;
import com.tuyoo.framework.grow.admin.service.GameService;
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
public class GameServiceImp implements GameService
{
    @Autowired
    GameRepository gameRepository;

    @Override
    public Page<GameEntities> fetch(Integer page, Integer size, String name)
    {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");

        Specification<GameEntities> specification = (Specification<GameEntities>) (root, criteriaQuery, cb) ->
        {
            List<Predicate> predicates = new ArrayList<>();

            if (!StringUtils.isEmpty(name))
            {
                predicates.add(cb.like(root.get("name").as(String.class), "%" + name + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return gameRepository.findAll(specification, pageable);
    }

    @Override
    public boolean create(CreateGameForm createGameForm)
    {
        if (gameRepository.findByProjectId(createGameForm.getProjectId()) != null) {
            return false;
        }
        gameRepository.save(createGameForm.entities(new GameEntities()));
        return true;
    }

    @Override
    public boolean update(EditGameForm editGameForm)
    {
        GameEntities game = gameRepository.findByProjectId(editGameForm.getProjectId());
        if (game == null) {
            return false;
        }
        gameRepository.save(editGameForm.entities(game));
        return true;
    }

    @Override
    public boolean delete(String projectId)
    {
        if (projectId == null) {
            return false;
        }
        GameEntities game = gameRepository.findByProjectId(projectId);
        if (game == null) {
            return false;
        }
        gameRepository.deleteById(game.getId());
        return true;
    }
}
