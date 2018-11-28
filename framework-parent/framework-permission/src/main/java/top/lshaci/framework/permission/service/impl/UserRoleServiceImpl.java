package top.lshaci.framework.permission.service.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.permission.mapper.UserRoleMapper;
import top.lshaci.framework.permission.model.UserRole;
import top.lshaci.framework.permission.service.UserRoleService;

@Slf4j
@Service
public class UserRoleServiceImpl implements UserRoleService {
	
	private UserRoleMapper userRoleMapper;
	
	@Autowired
	public void setUserRoleMapper(UserRoleMapper userRoleMapper) {
		this.userRoleMapper = userRoleMapper;
		this.userRoleMapper.createTable();
	}

	@Override
	@Transactional
	public void insertList(List<UserRole> userRoles) {
		if (CollectionUtils.isNotEmpty(userRoles)) {
			userRoleMapper.insertList(userRoles);
		} else {
			log.warn("This userRole list is empty!");
		}
	}

}
