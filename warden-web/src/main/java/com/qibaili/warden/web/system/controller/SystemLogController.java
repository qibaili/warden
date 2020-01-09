package com.qibaili.warden.web.system.controller;

import com.qibaili.warden.web.system.annotation.SystemLogs;
import com.qibaili.warden.web.system.common.bean.ResponseCode;
import com.qibaili.warden.web.system.common.bean.ResponseResult;
import com.qibaili.warden.web.system.dto.LogFindDTO;
import com.qibaili.warden.web.system.entity.SystemLog;
import com.qibaili.warden.web.system.service.SystemLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qibaili
 * @date 2019/9/24
 */
@RestController
@RequestMapping("system/log")
@Api(tags = {"操作日志"})
public class SystemLogController {

    @Autowired
    private SystemLogService logService;

    @GetMapping
    @ApiOperation("查询操作日志")
    @SystemLogs("查询操作日志")
    @RequiresPermissions("system:log:list")
    public ResponseResult getList(LogFindDTO dto) {
        Pageable pageable = PageRequest.of(dto.getPage() <= 0 ? 0 : dto.getPage() - 1, dto.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createTime"));
        Page<SystemLog> page = logService.findAll(dto, pageable);
        return ResponseResult.e(ResponseCode.OK, true, page);
    }
}
