package com.qibaili.warden.web.system.common.handler;

import com.qibaili.warden.web.system.common.bean.ResponseCode;
import com.qibaili.warden.web.system.common.bean.ResponseResult;
import com.qibaili.warden.web.system.common.exception.RequestException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理
 *
 * @author qibaili
 * @date 2018-04-04
 */
@ControllerAdvice
@Slf4j
@ResponseBody
public class GlobalDefaultExceptionHandler {

    @ExceptionHandler(RequestException.class)
    public ResponseResult requestExceptionHandler(RequestException e) {
        if (e.getE() != null) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return ResponseResult.builder().message(e.getMessage()).status(e.getStatus()).success(false).build();
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseResult requestExceptionHandler(AuthorizationException e) {
        return ResponseResult.builder().message("无操作权限！").status(ResponseCode.FAIL.code).success(false).build();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseResult requestExceptionHandler(DataIntegrityViolationException e) {
        return ResponseResult.builder().message("数据操作格式异常").status(ResponseCode.FAIL.code).success(false).build();
    }

    @ExceptionHandler(BindException.class)
    public ResponseResult bindExceptionHandler(BindException e) {
        BindingResult result = e.getBindingResult();
        return ResponseResult.builder().status(ResponseCode.FAIL.code).message(result.getFieldError().getDefaultMessage()).success(false).build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseResult requestExceptionHandler(Exception e) {
        e.printStackTrace();
        return ResponseResult.builder().message("服务器飘了，管理员去拿刀修理了~").status(ResponseCode.FAIL.code).success(false).build();
    }
}
