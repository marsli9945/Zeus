package com.tuyoo.framework.grow.admin.form.tmp;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DelForm
{
    @NotBlank(message = "token不能为空")
    String token;

    @NotBlank(message = "用户名不能为空")
    String username;
}
