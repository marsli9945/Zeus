package com.tuyoo.framework.grow.auth.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@ApiModel("令牌刷新表单")
public class RefreshForm
{
    @NotBlank(message = "令牌不能为空")
    @ApiModelProperty(value = "jwt令牌", name = "token", required = true, example = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODgwNDYzMTEsInVzZXJfbmFtZSI6ImFkbWluMDAxIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdLCJqdGkiOiI3ODBlMjViYS1iMTQ0LTQzN2QtODY2ZC1lYTRlYjk0MzNiOWYiLCJjbGllbnRfaWQiOiJuZXQ1aWp5Iiwic2NvcGUiOlsiYWxsIiwicmVhZCIsIndyaXRlIl19.CBXMQCdOTF8Qg0FKUuie4IAbbPjykY4XKqWoHbHpwRqhogcppkWPiyTiKZdHlxuNiLxsdA-7bQ5pVw7pVj9pxr5MFsS4JkKaBL_iM0m0pTYF5fWBB_rcPnJxXBumdMvZdqgPYBEg_CHyMcaEy8BSWx_c5MpmaD-YecODbZrI9pCnRh0gyIrR-ybsWLCWZtxBg5EuFuGIZAZuxHlwKOBtsMy3BKi0KWGWFmswOyCekHYa1SC9asu8_Ao6OBIpxMCxtCPRkRYQ1_iplN5FveLDr_vp42tuPZuretPqjfADE8ldJLdRNKESfqlou7NsfunKtFmfw9lpPQmgeR4kBLsz9A")
    private String token;

    @ApiModelProperty(value = "授权ID", name = "clientId", required = true, example = "test_client")
    @NotBlank(message = "授权ID不能为空")
    private String clientId;

    @ApiModelProperty(value = "授权密码", name = "clientSecret", required = true, example = "123456")
    @NotBlank(message = "授权密码不能为空")
    @Size(min = 6,max = 18,message = "密码的长度范围为6-18位")
    private String clientSecret;

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public String getClientId()
    {
        return clientId;
    }

    public void setClientId(String clientId)
    {
        this.clientId = clientId;
    }

    public String getClientSecret()
    {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret)
    {
        this.clientSecret = clientSecret;
    }
}
