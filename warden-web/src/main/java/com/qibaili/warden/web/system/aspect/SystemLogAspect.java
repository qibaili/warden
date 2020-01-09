package com.qibaili.warden.web.system.aspect;

import com.alibaba.fastjson.JSON;
import com.qibaili.warden.web.system.annotation.SystemLogs;
import com.qibaili.warden.web.system.dto.LoginDTO;
import com.qibaili.warden.web.system.dto.UserAddDTO;
import com.qibaili.warden.web.system.entity.SystemLog;
import com.qibaili.warden.web.system.entity.SystemUser;
import com.qibaili.warden.web.system.repository.SystemLogRepository;
import com.qibaili.warden.web.system.shiro.ShiroUtils;
import com.qibaili.warden.web.system.util.RequestUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author qibaili
 * @date 2018-08-14
 */
@Aspect
@Component
public class SystemLogAspect {

    @Autowired
    private SystemLogRepository logRepository;

    @Pointcut("@annotation(com.qibaili.warden.web.system.annotation.SystemLogs)")
    public void log() {
    }

    @AfterReturning("log()")
    public void after(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        SystemUser user = ShiroUtils.getUser();
        SystemLog sysLog = new SystemLog();
        sysLog.setAction(getMethodSysLogsAnnotationValue(joinPoint));
        sysLog.setIp(RequestUtils.getClientIp(request));
        sysLog.setAjax(RequestUtils.ajax(request) ? 1 : 0);
        sysLog.setUri(request.getRequestURI());
        String s = this.paramFilter(joinPoint.getArgs());
        sysLog.setParams(s.length() > 1000 ? "数据过大，不给予记录" : s);
        sysLog.setHttpMethod(request.getMethod());
        sysLog.setClassMethod(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName() + "()");
        if (user != null) {
            sysLog.setUsername(user.getUsername());
            sysLog.setUserId(user.getId());
        } else {
            sysLog.setUsername("游客");
            sysLog.setUserId("0");
        }
        sysLog.setCreateTime(new Date());
        logRepository.save(sysLog);
    }

    private String getMethodSysLogsAnnotationValue(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method.isAnnotationPresent(SystemLogs.class)) {
            SystemLogs logs = method.getAnnotation(SystemLogs.class);
            return logs.value();
        }
        return "未知";
    }

    private String paramFilter(Object[] params) {
        final String filterString = "******";
        if (params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                if (params[i] instanceof LoginDTO) {
                    LoginDTO loginDTO = (LoginDTO) params[i];
                    loginDTO.setPassword(filterString);
                    params[i] = loginDTO;
                }
                if (params[i] instanceof UserAddDTO) {
                    UserAddDTO userAddDTO = (UserAddDTO) params[i];
                    userAddDTO.setPassword(filterString);
                    params[i] = userAddDTO;
                }
                // if (params[i] instanceof ResetPasswordDTO) {
                //     ResetPasswordDTO resetPasswordDTO = (ResetPasswordDTO) params[i];
                //     resetPasswordDTO.setPassword(filterString);
                //     params[i] = resetPasswordDTO;
                // }
            }
        }
        return JSON.toJSONString(params);
    }
}
