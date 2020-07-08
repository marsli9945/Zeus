package com.tuyoo.framework.grow.admin.controller;

import com.tuyoo.framework.grow.admin.form.PageForm;
import com.tuyoo.framework.grow.admin.form.studio.CreateStudioForm;
import com.tuyoo.framework.grow.admin.form.studio.EditStudioForm;
import com.tuyoo.framework.grow.admin.service.StudioService;
import com.tuyoo.framework.grow.common.entities.ResultEntities;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("studio")
@Api(tags = "工作室管理接口")
public class StudioController
{
    @Autowired
    StudioService studioService;

    @GetMapping
    @ApiOperation(value = "获取工作室列表", notes = "获取工作室列表接口", response = ResultEntities.class)
    public ResultEntities<Object> fetch(@Validated PageForm pageForm, String name)
    {
        return ResultEntities.success(studioService.fetch(pageForm.getPage(), pageForm.getSize(), name));
    }

    @PostMapping
    @ApiOperation(value = "添加工作室", notes = "添加工作室接口", response = ResultEntities.class)
    public ResultEntities<Object> create(@RequestBody @Validated CreateStudioForm createStudioForm)
    {
        if (studioService.create(createStudioForm))
        {
            return ResultEntities.success();
        }
        return ResultEntities.failed();
    }

    @PutMapping
    @ApiOperation(value = "编辑工作室", notes = "编辑工作室接口", response = ResultEntities.class)
    public ResultEntities<Object> edit(@RequestBody @Validated EditStudioForm editStudioForm)
    {
        if (studioService.update(editStudioForm))
        {
            return ResultEntities.success();
        }
        return ResultEntities.failed();
    }

    @DeleteMapping
    @ApiOperation(value = "删除工作室", notes = "删除工作室接口", response = ResultEntities.class)
    public ResultEntities<Object> delete(@RequestParam Integer id){
        if (studioService.delete(id))
        {
            return ResultEntities.success();
        }
        return ResultEntities.failed();
    }
}
