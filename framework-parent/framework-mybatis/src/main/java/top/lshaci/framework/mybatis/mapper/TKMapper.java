package top.lshaci.framework.mybatis.mapper;

import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.RowBoundsMapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

/**
 * Base common mapper
 * 
 * @author lshaci
 *
 * @param <T> The entity type
 * @version 0.0.1
 */
public interface TKMapper<T> extends Mapper<T>, RowBoundsMapper<T>, InsertListMapper<T>, IdsMapper<T> {
	
}
