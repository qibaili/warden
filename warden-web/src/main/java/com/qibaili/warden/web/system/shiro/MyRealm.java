package com.qibaili.warden.web.system.shiro;

import com.qibaili.warden.web.system.bean.UserPermission;
import com.qibaili.warden.web.system.common.bean.Constants;
import com.qibaili.warden.web.system.entity.SystemUser;
import com.qibaili.warden.web.system.repository.SystemUserRepository;
import com.qibaili.warden.web.system.service.SystemResourceService;
import com.qibaili.warden.web.system.service.SystemUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author qibaili
 */
@Slf4j
public class MyRealm extends AuthorizingRealm {

    @Autowired
    private SystemUserRepository userRepository;

    @Autowired
    private SystemResourceService resourceService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Session session = SecurityUtils.getSubject().getSession();
        // 查询用户的权限
        UserPermission userPermission = (UserPermission) session.getAttribute(Constants.SESSION_USER_PERMISSION);
        // 为当前用户设置角色和权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addStringPermissions(userPermission.getPermissions());
        return simpleAuthorizationInfo;
    }

    /**
     * 验证当前登录的 Subject
     * LoginController 方法中执行 Subject.login() 时 执行此方法
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String username = usernamePasswordToken.getUsername();

        SystemUser user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UnknownAccountException();
        }
        ByteSource byteSource = ByteSource.Util.bytes(user.getSalt());
        String saltPassword = ShiroUtils.saltPassword(usernamePasswordToken.getPassword(), user.getSalt());
        if (!saltPassword.equals(user.getPassword())) {
            throw new IncorrectCredentialsException();
        }
        if (user.getStatus().equals(SystemUserService.DISABLE)) {
            throw new LockedAccountException();
        }

        // 将用户信息放入 Session 中
        UserPermission userPermission = resourceService.getUserPermissionByUsername(username);
        SecurityUtils.getSubject().getSession().setAttribute(Constants.SESSION_USER_PERMISSION, userPermission);
        SecurityUtils.getSubject().getSession().setTimeout(1800000);
        PrincipalCollection principalCollection = new SimplePrincipalCollection(user, getName());
        return new SimpleAuthenticationInfo(principalCollection, usernamePasswordToken.getPassword(), byteSource);
    }
}
