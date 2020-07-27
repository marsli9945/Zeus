package com.tuyoo.framework.grow.admin.service;

import com.tuyoo.framework.grow.admin.entities.GameEntities;
import com.tuyoo.framework.grow.admin.form.game.CreateGameForm;
import com.tuyoo.framework.grow.admin.form.game.EditGameForm;
import com.tuyoo.framework.grow.admin.ga.entities.GaGameInfoEntities;
import org.springframework.data.domain.Page;

public interface GameService
{
    Page<GameEntities> fetch(Integer page, Integer size, String name);

    boolean create(CreateGameForm createGameForm);

    GaGameInfoEntities info(String projectId);

    boolean update(EditGameForm editGameForm);

    boolean delete(String projectId);
}
