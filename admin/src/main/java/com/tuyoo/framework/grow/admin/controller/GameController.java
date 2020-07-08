package com.tuyoo.framework.grow.admin.controller;

import com.tuyoo.framework.grow.admin.form.PageForm;
import com.tuyoo.framework.grow.admin.form.game.CreateGameForm;
import com.tuyoo.framework.grow.admin.form.game.EditGameForm;
import com.tuyoo.framework.grow.admin.service.GameService;
import com.tuyoo.framework.grow.common.entities.ResultEntities;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("game")
@Api(tags = "游戏管理接口")
public class GameController
{
    @Autowired
    GameService gameService;

    @GetMapping
    @ApiOperation(value = "获取游戏列表", notes = "获取游戏列表接口", response = ResultEntities.class)
    public ResultEntities<Object> fetch(@Validated PageForm pageForm, String name)
    {
        return ResultEntities.success(gameService.fetch(pageForm.getPage(), pageForm.getSize(), name));
    }

    @PostMapping
    @ApiOperation(value = "添加游戏", notes = "添加游戏接口", response = ResultEntities.class)
    public ResultEntities<Object> create(@RequestBody @Validated CreateGameForm createGameForm)
    {
        if (gameService.create(createGameForm))
        {
            return ResultEntities.success();
        }
        return ResultEntities.failed();
    }

    @PutMapping
    @ApiOperation(value = "编辑游戏", notes = "编辑游戏接口", response = ResultEntities.class)
    public ResultEntities<Object> edit(@RequestBody @Validated EditGameForm editGameForm)
    {
        if (gameService.update(editGameForm))
        {
            return ResultEntities.success();
        }
        return ResultEntities.failed();
    }

    @DeleteMapping
    @ApiOperation(value = "删除游戏", notes = "删除游戏接口", response = ResultEntities.class)
    public ResultEntities<Object> delete(@RequestParam String projectId){
        if (gameService.delete(projectId))
        {
            return ResultEntities.success();
        }
        return ResultEntities.failed();
    }
}
