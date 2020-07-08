package com.tuyoo.framework.grow.admin.controller;

import com.tuyoo.framework.grow.admin.form.PageForm;
import com.tuyoo.framework.grow.admin.form.map.CreateMapForm;
import com.tuyoo.framework.grow.admin.form.map.EditMapForm;
import com.tuyoo.framework.grow.admin.service.MapService;
import com.tuyoo.framework.grow.common.entities.ResultEntities;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("map")
@Api(tags = "字典管理接口")
public class MapController
{
    @Autowired
    MapService mapService;

    @GetMapping
    @ApiOperation(value = "获取字典列表", notes = "获取字典列表接口", response = ResultEntities.class)
    public ResultEntities<Object> fetch(@Validated PageForm pageForm, String name)
    {
        return ResultEntities.success(mapService.fetch(pageForm.getPage(), pageForm.getSize(), name));
    }

    @PostMapping
    @ApiOperation(value = "添加字典", notes = "添加字典接口", response = ResultEntities.class)
    public ResultEntities<Object> create(@RequestBody @Validated CreateMapForm createMapForm)
    {
        if (mapService.create(createMapForm))
        {
            return ResultEntities.success();
        }
        return ResultEntities.failed();
    }

    @PutMapping
    @ApiOperation(value = "编辑字典", notes = "编辑字典接口", response = ResultEntities.class)
    public ResultEntities<Object> edit(@RequestBody @Validated EditMapForm editMapForm)
    {
        if (mapService.update(editMapForm))
        {
            return ResultEntities.success();
        }
        return ResultEntities.failed();
    }

    @DeleteMapping
    @ApiOperation(value = "删除字典", notes = "删除字典接口", response = ResultEntities.class)
    public ResultEntities<Object> delete(@RequestParam Integer id){
        if (mapService.delete(id))
        {
            return ResultEntities.success();
        }
        return ResultEntities.failed();
    }
}
