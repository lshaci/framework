package top.lshaci.framework.permission.service.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.common.exception.BaseException;
import top.lshaci.framework.permission.mapper.RoleMapper;
import top.lshaci.framework.permission.model.Role;
import top.lshaci.framework.permission.service.RoleService;

@Slf4j
@Service
public class RoleServiceImpl implements RoleService {
	
	private RoleMapper roleMapper;
	
	@Autowired
	public void setRoleMapper(RoleMapper roleMapper) {
		this.roleMapper = roleMapper;
		this.roleMapper.createTable();
	}

	@Override
	@Transactional
	public 	void insertList(List<Role> roles) {
		if (CollectionUtils.isNotEmpty(roles)) {
			roleMapper.insertList(roles);
		} else {
			log.warn("This role list is empty!");
		}
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Role> selectByUser(String user) {
		if (StringUtils.isBlank(user)) {
			throw new BaseException("The unique user id is blank!");
		}
		return roleMapper.selectByUser(user);
	}

}
