package top.lshaci.framework.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import top.lshaci.framework.common.constants.Constants;
import top.lshaci.framework.common.model.PageResult;
import top.lshaci.framework.mybatis.mapper.TKMapper;
import top.lshaci.framework.mybatis.model.MybatisPageResult;
import top.lshaci.framework.service.exception.BaseServiceException;

/**
 * Base common service implement<br><br>
 * 
 * <b>0.0.4:</b>Add method: insertSelective, updateSelective; 
 *              Modify transactional isolation and propagation level to default(history: READ_COMMITTED)
 * 
 * @author lshaci
 * @since 0.0.1
 * @version 0.0.4
 *
 * @param <T>	The entity type
 * @param <M>	The mapper type
 */
@Transactional(rollbackFor = Exception.class)
public abstract class BaseServiceImpl<T, M extends TKMapper<T>> implements BaseService<T, M> {
	
	@Autowired
	protected M mapper;

	@Override
	public int insert(T entity) {
		Objects.requireNonNull(entity, "The entity must not be null!");
		return mapper.insert(entity);
	}
	
	@Override
	public int insertSelective(T entity) {
		Objects.requireNonNull(entity, "The entity must not be null!");
		return mapper.insertSelective(entity);
	}

	@Override
	public int insertList(List<T> entities) {
		if (CollectionUtils.isEmpty(entities)) {
			throw new BaseServiceException("The entities must not be empty!");
		}
		return mapper.insertList(entities);
	}

	@Override
	public int update(T entity) {
		Objects.requireNonNull(entity, "The entity must not be null!");
		return mapper.updateByPrimaryKey(entity);
	}
	
	@Override
	public int updateSelective(T entity) {
		Objects.requireNonNull(entity, "The entity must not be null!");
		return mapper.updateByPrimaryKeySelective(entity);
	}

	@Override
	public int delete(Object primarykey) {
		Objects.requireNonNull(primarykey, "The primarykey must not be null!");
		return mapper.deleteByPrimaryKey(primarykey);
	}

	@Override
	public int deleteByIds(List<Object> primarykeys) {
		if (CollectionUtils.isEmpty(primarykeys)) {
			throw new BaseServiceException("The primarykeys must not be empty!");
		}
		return mapper.deleteByIds(repalceList2String(primarykeys));
	}

	@Override
	public int deleteByCondition(T condition) {
		Objects.requireNonNull(condition, "The condition must not be null!");
		return mapper.delete(condition);
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	@Override
	public T get(Object primarykey) {
		Objects.requireNonNull(primarykey, "The primarykey must not be null!");
		return mapper.selectByPrimaryKey(primarykey);
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	@Override
	public T getByCondition(T condition) {
		Objects.requireNonNull(condition, "The condition must not be null!");
		return mapper.selectOne(condition);
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	@Override
	public List<T> listByIds(List<Object> primarykeys) {
		if (CollectionUtils.isEmpty(primarykeys)) {
			throw new BaseServiceException("The primarykeys must not be empty!");
		}
		return mapper.selectByIds(repalceList2String(primarykeys));
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	@Override
	public List<T> listAll() {
		return mapper.selectAll();
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	@Override
	public List<T> listByCondition(T condition) {
		Objects.requireNonNull(condition, "The condition must not be null!");
		return mapper.select(condition);
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	@Override
	public int countByCondition(T condition) {
		return mapper.selectCount(condition);
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	@Override
	public PageResult<T> listPage(int pageNum, int pageSize) {
		return listPageWithCondition(pageNum, pageSize, null);
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	@Override
	public PageResult<T> listPageWithCondition(int pageNum, int pageSize, T condition) {
		pageNum = pageNum <= 0 ? Constants.DEFAULT_PGCT: pageNum;
		pageSize = pageSize <= 0 ? Constants.DEFAULT_PGSZ: pageSize;
		
		PageHelper.startPage(pageNum, pageSize);
		Page<T> page = (Page<T>) mapper.select(condition);
		
		if (CollectionUtils.isEmpty(page)) {
			return new PageResult<>(pageNum, pageSize);
		}
		return new MybatisPageResult<>(page);
	}
	
	/**
	 * Replace list primary keys to string
	 * 
	 * @param primarykeys the primary keys
	 * @return the string of the primary keys
	 */
	private String repalceList2String(List<? extends Object> primarykeys) {
		return primarykeys.stream().map(Object::toString).collect(Collectors.joining(","));
    }
	
}
