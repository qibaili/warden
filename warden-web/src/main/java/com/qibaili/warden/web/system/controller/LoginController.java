package com.qibaili.warden.web.system.controller;

import com.qibaili.warden.web.system.annotation.SystemLogs;
import com.qibaili.warden.web.system.common.bean.ResponseResult;
import com.qibaili.warden.web.system.dto.LoginDTO;
import com.qibaili.warden.web.system.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qibaili
 * @date 2018-06-08
 */
@RestController
@Api(tags = {"登录管理"})
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("login")
    @ApiOperation(value = "登录")
    @SystemLogs("登录")
    public ResponseResult login(@Validated LoginDTO dto) {
        return loginService.login(dto);
    }

    @PostMapping("current")
    @ApiOperation(value = "获取当前登录用户信息")
    public ResponseResult getInfo() {
        return loginService.getInfo();
    }

    @PostMapping("logout")
    @ApiOperation(value = "注销登录")
    public ResponseResult logout() {
        return loginService.logout();
    }
}
