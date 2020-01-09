package com.qibaili.warden.web.system.controller;

import com.qibaili.warden.web.system.annotation.SystemLogs;
import com.qibaili.warden.web.system.common.bean.ResponseCode;
import com.qibaili.warden.web.system.common.bean.ResponseResult;
import com.qibaili.warden.web.system.dto.PageDTO;
import com.qibaili.warden.web.system.dto.RoleAddDTO;
import com.qibaili.warden.web.system.dto.RoleUpdateDTO;
import com.qibaili.warden.web.system.entity.SystemRole;
import com.qibaili.warden.web.system.service.SystemRoleService;
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
@RequestMapping("system/role")
@Api(tags = {"角色管理"})
public class SystemRoleController {

    @Autowired
    private SystemRoleService roleService;

    @GetMapping
    @ApiOperation(value = "查询角色")
    @SystemLogs("查询角色")
    @RequiresPermissions("system:role:list")
    public ResponseResult getList(String name, PageDTO dto) {
        Pageable pageable = PageRequest.of(dto.getPage() <= 0 ? 0 : dto.getPage() - 1, dto.getPageSize(),
                Sort.by(Sort.Direction.ASC, "createTime"));
        Page<SystemRole> page = roleService.findAll(name, pageable);
        return ResponseResult.e(ResponseCode.OK, true, page);
    }

    @GetMapping("{userId}")
    @ApiOperation(value = "根据 Userid 查询角色")
    public ResponseResult getListByUserid(@PathVariable String userId) {
        List<SystemRole> list = roleService.findByUserId(userId);
        return ResponseResult.e(ResponseCode.OK, true, list);
    }

    @PostMapping
    @ApiOperation("新建角色")
    @SystemLogs("新建角色")
    @RequiresPermissions("system:role:add")
    public ResponseResult createRole(@Validated RoleAddDTO dto) {
        roleService.save(dto);
        return ResponseResult.e(ResponseCode.OK, true);
    }

    @PutMapping("{id}")
    @ApiOperation("更新角色")
    @SystemLogs("更新角色")
    @RequiresPermissions("system:role:update")
    public ResponseResult updateRole(@PathVariable String id, @Validated RoleUpdateDTO dto) {
        roleService.update(id, dto);
        return ResponseResult.e(ResponseCode.OK, true);
    }

    @PutMapping("resource/{id}")
    @ApiOperation("角色分配权限")
    @SystemLogs("角色分配权限")
    @RequiresPermissions("system:role:update")
    public ResponseResult assignPermissions(@PathVariable String id,
                                            @RequestParam(value = "resourceIds", required = false) List<String> resourceIds) {
        roleService.assignPermissions(id, resourceIds);
        return ResponseResult.e(ResponseCode.OK, true);
    }
}