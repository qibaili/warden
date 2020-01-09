package com.qibaili.warden.web.system.service.impl;

import com.qibaili.warden.web.system.dto.LogFindDTO;
import com.qibaili.warden.web.system.entity.SystemLog;
import com.qibaili.warden.web.system.repository.SystemLogRepository;
import com.qibaili.warden.web.system.service.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;

/**
 * @author qibaili
 * @date 2018-08-14
 */
@Service
public class SystemLogServiceImpl implements SystemLogService {

    @Autowired
    private SystemLogRepository logRepository;

    @Override
    public Page<SystemLog> findAll(LogFindDTO dto, Pageable pageable) {
        Specification<SystemLog> specification = createSpecification(dto);
        return logRepository.findAll(specification, pageable);
    }

    private Specification<SystemLog> createSpecification(LogFindDTO dto) {
        return (Specification<SystemLog>) (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (!StringUtils.isEmpty(dto.getUsername())) {
                predicate.getExpressions().add(criteriaBuilder.equal(root.get("username"), dto.getUsername()));
            }
            if (!StringUtils.isEmpty(dto.getAction())) {
                predicate.getExpressions().add(criteriaBuilder.equal(root.get("action"), dto.getAction()));
            }
            return predicate;
        };
    }
}
