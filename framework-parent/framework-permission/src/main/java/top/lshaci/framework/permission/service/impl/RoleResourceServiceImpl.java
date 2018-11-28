package top.lshaci.framework.permission.service.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.permission.mapper.RoleResourceMapper;
import top.lshaci.framework.permission.model.RoleResource;
import top.lshaci.framework.permission.service.RoleResourceService;

@Slf4j
@Service
public class RoleResourceServiceImpl implements RoleResourceService {

	
	private RoleResourceMapper roleResourceMapper;
	
	@Autowired
	public void setRoleResourceMapper(RoleResourceMapper roleResourceMapper) {
		this.roleResourceMapper = roleResourceMapper;
		this.roleResourceMapper.createTable();
	}

	@Override
	@Transactional
	public 	void insertList(List<RoleResource> roleResources) {
		if (CollectionUtils.isNotEmpty(roleResources)) {
			roleResourceMapper.insertList(roleResources);
		} else {
			log.warn("This roleResource list is empty!");
		}
	}

}
