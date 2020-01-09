package com.qibaili.warden.web.system.service.impl;

import com.qibaili.warden.web.system.bean.UserPermission;
import com.qibaili.warden.web.system.common.bean.Constants;
import com.qibaili.warden.web.system.common.bean.ResponseCode;
import com.qibaili.warden.web.system.common.bean.ResponseResult;
import com.qibaili.warden.web.system.common.exception.RequestException;
import com.qibaili.warden.web.system.dto.LoginDTO;
import com.qibaili.warden.web.system.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author qibaili
 * @date 2018-06-08
 */
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    @Override
    public ResponseResult login(LoginDTO dto) {
        if (StringUtils.isEmpty(dto.getUsername()) || StringUtils.isEmpty(dto.getPassword())) {
            throw new RequestException(ResponseCode.LOGIN_INPUT_EMPTY);
        }
        // SecurityUtils.getSubject() 是每个请求创建一个 Subject，
        // 并保存到 ThreadContext 的 resources（ThreadLocal<Map<Object, Object>>）变量中，
        // 也就是一个 http 请求一个 Subject，并绑定到当前线程。
        try {
            SecurityUtils.getSubject().login(new UsernamePasswordToken(dto.getUsername(), dto.getPassword()));

            return ResponseResult.e(ResponseCode.LOGIN_OK, true);
        } catch (UnknownAccountException | IncorrectCredentialsException e) {
            log.error("登录失败！" + dto);
            return ResponseResult.e(ResponseCode.LOGIN_INPUT_FAIL, false);
        } catch (LockedAccountException e) {
            return ResponseResult.e(ResponseCode.USER_STATUS_ERROR, false);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseResult.e(ResponseCode.FAIL, false);
        }
    }

    @Override
    public ResponseResult getInfo() {
        Session session = SecurityUtils.getSubject().getSession();
        UserPermission permission = (UserPermission) session.getAttribute(Constants.SESSION_USER_PERMISSION);
        return ResponseResult.e(ResponseCode.OK, true, permission);
    }

    @Override
    public ResponseResult logout() {
        try {
            Subject subject = SecurityUtils.getSubject();
            subject.logout();
            return ResponseResult.e(ResponseCode.LOGOUT_OK, true);
        } catch (Exception e) {
            throw new RequestException(ResponseCode.LOGOUT_FAIL, e);
        }
    }
}
