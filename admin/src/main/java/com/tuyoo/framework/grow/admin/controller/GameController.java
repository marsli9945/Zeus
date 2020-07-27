package com.tuyoo.framework.grow.admin.controller;

import com.tuyoo.framework.grow.admin.entities.GameEntities;
import com.tuyoo.framework.grow.admin.form.InitForm;
import com.tuyoo.framework.grow.admin.form.PageForm;
import com.tuyoo.framework.grow.admin.form.game.CreateGameForm;
import com.tuyoo.framework.grow.admin.form.game.EditGameForm;
import com.tuyoo.framework.grow.admin.ga.entities.GaGameInfoEntities;
import com.tuyoo.framework.grow.admin.minio.MinioFileUploade;
import com.tuyoo.framework.grow.admin.service.GameService;
import com.tuyoo.framework.grow.admin.service.InitService;
import com.tuyoo.framework.grow.common.entities.ResultEntities;
import io.minio.errors.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@RestController
@RequestMapping("game")
@Api(tags = "游戏管理接口")
public class GameController
{
    @Autowired
    GameService gameService;

    @Autowired
    MinioFileUploade minioFileUploade;

    @Autowired
    InitService initService;

    @GetMapping
    @ApiOperation(value = "获取游戏列表", notes = "获取游戏列表接口", response = ResultEntities.class)
    public ResultEntities<Object> fetch(@Validated PageForm pageForm, String name)
    {
        return ResultEntities.success(gameService.fetch(pageForm.getPage(), pageForm.getSize(), name));
    }

    @PostMapping
    @ApiOperation(value = "添加游戏", notes = "添加游戏接口", response = ResultEntities.class)
    public ResultEntities<Object> create(@RequestBody @Validated CreateGameForm createGameForm, HttpServletRequest request)
    {
        if (gameService.create(createGameForm))
        {
            InitForm initForm = new InitForm();
            initForm.setGa_request_id(request.getHeader("ga_request_id"));
            initForm.setGa_model_name(request.getHeader("ga_model_name"));
            initForm.setGa_socket_name(request.getHeader("ga_socket_name"));
            initForm.setGa_user_id(request.getHeader("ga_user_id"));
            initForm.setGa_username(request.getHeader("ga_username"));
            initForm.setWeb_request_id(request.getHeader("web_request_id"));
            initForm.setGa_project_id(createGameForm.getProjectId());

            log.info("initForm:{}", initForm);
            log.info("init result:{}", initService.initProject(initForm));

            return ResultEntities.success();
        }
        return ResultEntities.failed();
    }

    @GetMapping("info")
    @ApiOperation(value = "获取游戏信息", notes = "获取游戏信息接口", response = GaGameInfoEntities.class)
    public ResultEntities<Object> info(@RequestParam String projectId)
    {
        return ResultEntities.success(gameService.info(projectId));
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

    @PostMapping("/upload")
    public ResultEntities<Object> upload(@RequestParam("file") MultipartFile file) throws IOException, XmlPullParserException, NoSuchAlgorithmException, RegionConflictException, InvalidKeyException, InvalidPortException, InvalidArgumentException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InvalidEndpointException, InternalException
    {
        log.info("访问到上传接口");
        if (file.isEmpty())
        {
            ResultEntities.failed("上传失败");
        }
        return ResultEntities.success(minioFileUploade.upload(file));
    }

    @DeleteMapping
    @ApiOperation(value = "删除游戏", notes = "删除游戏接口", response = ResultEntities.class)
    public ResultEntities<Object> delete(@RequestParam String projectId)
    {
        if (gameService.delete(projectId))
        {
            return ResultEntities.success();
        }
        return ResultEntities.failed();
    }
}
