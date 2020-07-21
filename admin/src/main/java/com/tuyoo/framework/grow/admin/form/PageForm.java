package com.tuyoo.framework.grow.admin.form;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "分页参数")
public class PageForm
{
    private Integer page = 0;
    private Integer size = 20;

    public Integer getPage() {
        return this.page - 1;
    }
}
