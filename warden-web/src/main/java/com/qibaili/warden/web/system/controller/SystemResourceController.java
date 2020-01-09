package com.qibaili.warden.web.system.controller;

import com.qibaili.warden.web.system.annotation.SystemLogs;
import com.qibaili.warden.web.system.common.bean.ResponseCode;
import com.qibaili.warden.web.system.common.bean.ResponseResult;
import com.qibaili.warden.web.system.dto.ResourceAddDTO;
import com.qibaili.warden.web.system.dto.ResourceUpdateDTO;
import com.qibaili.warden.web.system.entity.SystemResource;
import com.qibaili.warden.web.system.service.SystemResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author qibaili
 * @date 2018-06-10
 */
@RestController
@RequestMapping("system/resource")
@Api(tags = {"资源管理"})
public class SystemResourceController {

    @Autowired
    private SystemResourceService resourceService;

    @GetMapping
    @ApiOperation("查询资源")
    @SystemLogs("查询资源")
    @RequiresPermissions("system:resource:list")
    public ResponseResult getList() {
        List<SystemResource> list = resourceService.findAll();
        return ResponseResult.e(ResponseCode.OK, true, list);
    }

    @GetMapping("{roleId}")
    @ApiOperation("根据 RoleId 查询资源")
    @SystemLogs("根据 RoleId 查询资源")
    @RequiresPermissions("system:resource:list")
    public ResponseResult getListByRoleId(@PathVariable String roleId) {
        List<SystemResource> list = resourceService.findByRoleId(roleId);
        return ResponseResult.e(ResponseCode.OK, true, list);
    }

    @PostMapping
    @ApiOperation("新建资源")
    @SystemLogs("新建资源")
    @RequiresPermissions("system:resource:add")
    public ResponseResult createResource(@Validated ResourceAddDTO dto) {
        resourceService.save(dto);
        return ResponseResult.e(ResponseCode.OK, true);
    }

    @PutMapping("{id}")
    @ApiOperation("更新资源")
    @SystemLogs("更新资源")
    @RequiresPermissions("system:resource:update")
    public ResponseResult updateResource(@PathVariable String id, @Validated ResourceUpdateDTO dto) {
        resourceService.update(id, dto);
        return ResponseResult.e(ResponseCode.OK, true);
    }

    @DeleteMapping("{id}")
    @ApiOperation("删除资源")
    @SystemLogs("删除资源")
    @RequiresPermissions("system:resource:delete")
    public ResponseResult deleteResource(@PathVariable String id) {
        resourceService.delete(id);
        return ResponseResult.e(ResponseCode.OK, true);
    }

}
