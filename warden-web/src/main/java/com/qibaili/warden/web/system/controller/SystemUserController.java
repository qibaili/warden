package com.qibaili.warden.web.system.controller;

import com.qibaili.warden.web.system.annotation.SystemLogs;
import com.qibaili.warden.web.system.common.bean.ResponseCode;
import com.qibaili.warden.web.system.common.bean.ResponseResult;
import com.qibaili.warden.web.system.dto.UserAddDTO;
import com.qibaili.warden.web.system.dto.UserFindDTO;
import com.qibaili.warden.web.system.dto.UserPasswordDTO;
import com.qibaili.warden.web.system.dto.UserUpdateDTO;
import com.qibaili.warden.web.system.entity.SystemUser;
import com.qibaili.warden.web.system.service.SystemUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author qibaili
 * @date 2018-06-09
 */
@RestController
@RequestMapping("system/user")
@Api(tags = {"用户管理"})
public class SystemUserController {

    @Autowired
    private SystemUserService userService;

    @GetMapping
    @ApiOperation("查询用户")
    @SystemLogs("查询用户")
    @RequiresPermissions("system:user:list")
    public ResponseResult getList(UserFindDTO dto) {
        Pageable pageable = PageRequest.of(dto.getPage() <= 0 ? 0 : dto.getPage() - 1, dto.getPageSize(),
                Sort.by(Sort.Direction.ASC, "createTime"));
        Page<SystemUser> page = userService.findAll(dto, pageable);
        return ResponseResult.e(ResponseCode.OK, true, page);
    }

    @GetMapping("{roleId}")
    @ApiOperation(value = "根据 RoleId 查询用户")
    public ResponseResult getUserByRoleId(@PathVariable String roleId) {
        List<SystemUser> list = userService.findByRoleId(roleId);
        return ResponseResult.e(ResponseCode.OK, true, list);
    }

    @PostMapping
    @ApiOperation("新建用户")
    @SystemLogs("新建用户")
    @RequiresPermissions("system:user:add")
    public ResponseResult createUser(@Validated UserAddDTO dto) {
        userService.save(dto);
        return ResponseResult.e(ResponseCode.OK, true);
    }

    @PutMapping("{id}")
    @ApiOperation("更新用户")
    @SystemLogs("更新用户")
    @RequiresPermissions("system:user:update")
    public ResponseResult updateUser(@PathVariable String id, @Validated UserUpdateDTO dto) {
        userService.update(id, dto);
        return ResponseResult.e(ResponseCode.OK, true);
    }

    @PutMapping("status/{id}")
    @ApiOperation("禁用/启用用户")
    @SystemLogs("禁用/启用用户")
    @RequiresPermissions("system:user:update")
    public ResponseResult changeStatus(@PathVariable String id) {
        userService.changeStatus(id);
        return ResponseResult.e(ResponseCode.OK, true);
    }

    @PutMapping("role/{id}")
    @ApiOperation("用户分配角色")
    @SystemLogs("用户分配角色")
    @RequiresPermissions("system:user:update")
    public ResponseResult assigneRole(@PathVariable String id,
                                      @RequestParam(value = "roleIds", required = false) List<String> roleIds) {
        userService.assignRole(id, roleIds);
        return ResponseResult.e(ResponseCode.OK, true);
    }

    @PutMapping("resetpassword/{id}")
    @ApiOperation("重置密码")
    @SystemLogs("重置密码")
    @RequiresPermissions("system:user:password")
    public ResponseResult resetPassword(@PathVariable String id) {
        userService.resetPassword(id);
        return ResponseResult.e(ResponseCode.OK, true);
    }

    @PutMapping("changepassword/{id}")
    @ApiOperation("修改密码")
    @SystemLogs("修改密码")
    public ResponseResult changePassword(@PathVariable String id, @Validated UserPasswordDTO dto) {
        userService.changePassword(id, dto);
        return ResponseResult.e(ResponseCode.OK, true);
    }
}