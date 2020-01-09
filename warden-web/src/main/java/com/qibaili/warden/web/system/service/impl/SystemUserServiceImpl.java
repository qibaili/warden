package com.qibaili.warden.web.system.service.impl;

import com.qibaili.warden.web.system.common.exception.RequestException;
import com.qibaili.warden.web.system.dto.UserAddDTO;
import com.qibaili.warden.web.system.dto.UserFindDTO;
import com.qibaili.warden.web.system.dto.UserPasswordDTO;
import com.qibaili.warden.web.system.dto.UserUpdateDTO;
import com.qibaili.warden.web.system.entity.SystemUser;
import com.qibaili.warden.web.system.entity.SystemUserRole;
import com.qibaili.warden.web.system.repository.SystemRoleRepository;
import com.qibaili.warden.web.system.repository.SystemUserRepository;
import com.qibaili.warden.web.system.repository.SystemUserRoleRepository;
import com.qibaili.warden.web.system.service.SystemUserService;
import com.qibaili.warden.web.system.shiro.ShiroUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author qibaili
 * @date 2018-06-06
 */
@Service
public class SystemUserServiceImpl implements SystemUserService {

    @Autowired
    private SystemUserRepository userRepository;

    @Autowired
    private SystemRoleRepository roleRepository;

    @Autowired
    private SystemUserRoleRepository userRoleRepository;

    @Override
    public Page<SystemUser> findAll(UserFindDTO dto, Pageable pageable) {
        Specification<SystemUser> specification = createSpecification(dto);
        return userRepository.findAll(specification, pageable);
    }

    @Override
    public List<SystemUser> findByRoleId(String roleId) {
        return userRepository.findByRoleId(roleId);
    }

    @Override
    public void save(UserAddDTO dto) {
        SystemUser user = userRepository.findByUsername(dto.getUsername());
        if (user != null) {
            throw RequestException.fail(String.format("用户名[%s]已存在，请重新输入", dto.getUsername()));
        }
        try {
            SystemUser admin = (SystemUser) SecurityUtils.getSubject().getPrincipal();
            user = new SystemUser();
            BeanUtils.copyProperties(dto, user);
            user.setSalt(ShiroUtils.uuid());
            user.setPassword(ShiroUtils.saltPassword(user.getPassword(), user.getSalt()));
            user.setCreateTime(new Date());
            user.setStatus("1");
            user.setCreatePerson(admin.getUsername());
            userRepository.save(user);
        } catch (Exception e) {
            throw RequestException.fail("添加用户失败", e);
        }
    }

    @Override
    public void update(String id, UserUpdateDTO dto) {
        Optional<SystemUser> optional = userRepository.findById(id);
        if (!optional.isPresent()) {
            throw RequestException.fail(String.format("更新失败，不存在 ID 为[%s]的用户", id));
        }
        SystemUser user = optional.get();
        user.setEmail(dto.getEmail());
        user.setTelephone(dto.getTelephone());
        user.setUpdateTime(new Date());
        user.setUpdatePerson(ShiroUtils.getUser().getUsername());
        userRepository.save(user);
    }

    @Override
    public void changeStatus(String id) {
        Optional<SystemUser> optional = userRepository.findById(id);
        if (optional.isPresent()) {
            SystemUser user = optional.get();
            if (ShiroUtils.getUser().getId().equals(id)) {
                throw RequestException.fail("不能锁定自己的账户");
            }
            if (ENABLE.equals(user.getStatus())) {
                user.setStatus("0");
            } else if (DISABLE.equals(user.getStatus())) {
                user.setStatus("1");
            }
            userRepository.save(user);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRole(String id, List<String> roleIds) {
        if (!userRepository.findById(id).isPresent()) {
            throw RequestException.fail(String.format("用户关联角色失败，不存在 ID 为[%s]的用户", id));
        }
        if (roleIds != null && roleIds.size() > 0) {
            // 关联用户新角色
            for (String roleId : roleIds) {
                if (!roleRepository.findById(roleId).isPresent()) {
                    throw RequestException.fail(String.format("用户关联角色失败，不存在 ID 为[%s]的角色", roleId));
                }
                if (userRoleRepository.findByUserIdAndRoleId(id, roleId) != null) {
                    continue;
                }
                SystemUserRole userRole = new SystemUserRole();
                userRole.setUserId(id);
                userRole.setRoleId(roleId);
                userRole.setCreateTime(new Date());
                userRole.setCreatePerson(ShiroUtils.getUser().getUsername());
                userRoleRepository.save(userRole);
            }
            // 删除旧【用户-角色】的关联
            List<SystemUserRole> list = userRoleRepository.findByUserIdAndRoleIdNotIn(id, roleIds);
            if (list != null && list.size() > 0) {
                userRoleRepository.deleteAll(list);
            }
        } else {
            // 清除用户角色
            List<SystemUserRole> list = userRoleRepository.findByUserId(id);
            if (list != null && list.size() > 0) {
                userRoleRepository.deleteAll(list);
            }
        }
    }

    @Override
    public void resetPassword(String id) {
        Optional<SystemUser> optional = userRepository.findById(id);
        if (optional.isPresent()) {
            SystemUser user = optional.get();
            user.setPassword(ShiroUtils.saltPassword(INITIAL_PASSWORD, user.getSalt()));
            user.setUpdateTime(new Date());
            user.setUpdatePerson(ShiroUtils.getUser().getUsername());
        }
    }

    @Override
    public void changePassword(String id, UserPasswordDTO dto) {
        Optional<SystemUser> optional = userRepository.findById(id);
        if (optional.isPresent()) {
            SystemUser user = optional.get();
            if (!ShiroUtils.saltPassword(dto.getOldPassword(), user.getSalt()).equals(user.getPassword())) {
                throw RequestException.fail("原密码错误！");
            }
            user.setPassword(ShiroUtils.saltPassword(dto.getNewPassword(), user.getSalt()));
            user.setUpdateTime(new Date());
            user.setUpdatePerson(ShiroUtils.getUser().getUsername());
        }
    }

    private Specification<SystemUser> createSpecification(UserFindDTO dto) {
        return (Specification<SystemUser>) (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (!StringUtils.isEmpty(dto.getName())) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("name"), "%" + dto.getName() + "%"));
            }
            if (!StringUtils.isEmpty(dto.getUsername())) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("username"), "%" + dto.getUsername() + "%"));
            }
            return predicate;
        };
    }
}
