package com.qibaili.warden.web.system.service;

import com.qibaili.warden.web.system.common.bean.ResponseResult;
import com.qibaili.warden.web.system.dto.LoginDTO;

/**
 * @author qibaili
 * @date 2018-06-08
 */
public interface LoginService {

    ResponseResult login(LoginDTO dto);

    ResponseResult getInfo();

    ResponseResult logout();
}
