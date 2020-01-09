package com.qibaili.warden.web.system.shiro;

import com.qibaili.warden.web.system.entity.SystemUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;

import java.util.UUID;

/**
 * @author qibaili
 * @date 2018-04-14
 */
public class ShiroUtils {

    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String saltPassword(Object password, String salt) {
        return new SimpleHash("MD5", password, salt, 1024).toString();
    }

    public static SystemUser getUser() {
        return (SystemUser) SecurityUtils.getSubject().getPrincipal();
    }
}
