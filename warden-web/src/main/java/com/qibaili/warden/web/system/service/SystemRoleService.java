package com.qibaili.warden.web.system.service;

import com.qibaili.warden.web.system.dto.RoleAddDTO;
import com.qibaili.warden.web.system.dto.RoleUpdateDTO;
import com.qibaili.warden.web.system.entity.SystemRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author qibaili
 * @date 2018-06-06
 */
public interface SystemRoleService {

    Page<SystemRole> findAll(String name, Pageable pageable);

    void save(RoleAddDTO dto);

    void update(String id, RoleUpdateDTO dto);

    void assignPermissions(String id, List<String> resourceIds);

    List<SystemRole> findByUserId(String userId);
}
