package com.qibaili.warden.web;

import com.qibaili.warden.web.system.entity.SystemUser;
import com.qibaili.warden.web.system.shiro.ShiroUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WardenWebApplicationTests {

    @Test
    public void createSysUser() {
        SystemUser user = new SystemUser();
        String password = "system";
        user.setId(ShiroUtils.uuid());
        user.setSalt(ShiroUtils.uuid());
        user.setPassword(ShiroUtils.saltPassword(password, user.getSalt()));
        System.out.println(user);
    }

}
