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
import top.lshaci.framework.permission.mapper.ResourceMapper;
import top.lshaci.framework.permission.model.Resource;
import top.lshaci.framework.permission.service.ResourceService;

@Slf4j
@Service
public class ResourceServiceImpl implements ResourceService {
	
	private ResourceMapper resourceMapper;
	
	@Autowired
	public void setResourceMapper(ResourceMapper resourceMapper) {
		this.resourceMapper = resourceMapper;
		this.resourceMapper.createTable();
	}

	@Override
	@Transactional
	public void insertList(List<Resource> resources) {
		if (CollectionUtils.isNotEmpty(resources)) {
			resourceMapper.insertList(resources);
		} else {
			log.warn("This resource list is empty!");
		}
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Resource> selectByUser(String user) {
		if (StringUtils.isBlank(user)) {
			throw new BaseException("The unique user id is blank!");
		}
		return resourceMapper.selectByUser(user);
	}

}
