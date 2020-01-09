package com.qibaili.warden.web.system.service;

import com.qibaili.warden.web.system.dto.LogFindDTO;
import com.qibaili.warden.web.system.entity.SystemLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author qibaili
 * @date 2018-08-14
 */
public interface SystemLogService {

    Page<SystemLog> findAll(LogFindDTO dto, Pageable pageable);
}
