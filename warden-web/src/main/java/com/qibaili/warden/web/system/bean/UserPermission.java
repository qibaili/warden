package com.qibaili.warden.web.system.bean;

import com.qibaili.warden.web.system.entity.SystemUser;
import lombok.Data;

import java.util.List;

/**
 * @author qibaili
 * @date 2018-06-08
 */
@Data
public class UserPermission {

    private SystemUser user;

    private List<String> roles;

    private List<String> permissions;

}
