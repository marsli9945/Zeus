package com.tuyoo.framework.grow.auth.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("令牌返回实体")
public class TokenResponse
{
    @ApiModelProperty(value = "jwt令牌", name = "access_token", required = true, example = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODgwNDYzMTEsInVzZXJfbmFtZSI6ImFkbWluMDAxIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdLCJqdGkiOiI3ODBlMjViYS1iMTQ0LTQzN2QtODY2ZC1lYTRlYjk0MzNiOWYiLCJjbGllbnRfaWQiOiJuZXQ1aWp5Iiwic2NvcGUiOlsiYWxsIiwicmVhZCIsIndyaXRlIl19.CBXMQCdOTF8Qg0FKUuie4IAbbPjykY4XKqWoHbHpwRqhogcppkWPiyTiKZdHlxuNiLxsdA-7bQ5pVw7pVj9pxr5MFsS4JkKaBL_iM0m0pTYF5fWBB_rcPnJxXBumdMvZdqgPYBEg_CHyMcaEy8BSWx_c5MpmaD-YecODbZrI9pCnRh0gyIrR-ybsWLCWZtxBg5EuFuGIZAZuxHlwKOBtsMy3BKi0KWGWFmswOyCekHYa1SC9asu8_Ao6OBIpxMCxtCPRkRYQ1_iplN5FveLDr_vp42tuPZuretPqjfADE8ldJLdRNKESfqlou7NsfunKtFmfw9lpPQmgeR4kBLsz9A")
    private String access_token;

    @ApiModelProperty(value = "刷新用令牌", name = "refresh_token", required = true, example = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODgwNDYzMTEsInVzZXJfbmFtZSI6ImFkbWluMDAxIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdLCJqdGkiOiI3ODBlMjViYS1iMTQ0LTQzN2QtODY2ZC1lYTRlYjk0MzNiOWYiLCJjbGllbnRfaWQiOiJuZXQ1aWp5Iiwic2NvcGUiOlsiYWxsIiwicmVhZCIsIndyaXRlIl19.CBXMQCdOTF8Qg0FKUuie4IAbbPjykY4XKqWoHbHpwRqhogcppkWPiyTiKZdHlxuNiLxsdA-7bQ5pVw7pVj9pxr5MFsS4JkKaBL_iM0m0pTYF5fWBB_rcPnJxXBumdMvZdqgPYBEg_CHyMcaEy8BSWx_c5MpmaD-YecODbZrI9pCnRh0gyIrR-ybsWLCWZtxBg5EuFuGIZAZuxHlwKOBtsMy3BKi0KWGWFmswOyCekHYa1SC9asu8_Ao6OBIpxMCxtCPRkRYQ1_iplN5FveLDr_vp42tuPZuretPqjfADE8ldJLdRNKESfqlou7NsfunKtFmfw9lpPQmgeR4kBLsz9A")
    private String refresh_token;

    @ApiModelProperty(value = "权限列表", name = "scope", required = true, example = "all read write")
    private String scope;

    @ApiModelProperty(value = "令牌类型", name = "token_type", required = true, example = "bearer")
    private String token_type;

    @ApiModelProperty(value = "令牌类有效时间", name = "expires_in", required = true, example = "7199")
    private Integer expires_in;

    @ApiModelProperty(value = "令牌ID", name = "jti", required = true, example = "f0327947-37fa-4df2-8b6a-1bf9f4b68337")
    private String jti;

    public String getAccess_token()
    {
        return access_token;
    }

    public void setAccess_token(String access_token)
    {
        this.access_token = access_token;
    }

    public String getRefresh_token()
    {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token)
    {
        this.refresh_token = refresh_token;
    }

    public String getScope()
    {
        return scope;
    }

    public void setScope(String scope)
    {
        this.scope = scope;
    }

    public String getToken_type()
    {
        return token_type;
    }

    public void setToken_type(String token_type)
    {
        this.token_type = token_type;
    }

    public Integer getExpires_in()
    {
        return expires_in;
    }

    public void setExpires_in(Integer expires_in)
    {
        this.expires_in = expires_in;
    }

    public String getJti()
    {
        return jti;
    }

    public void setJti(String jti)
    {
        this.jti = jti;
    }
}
