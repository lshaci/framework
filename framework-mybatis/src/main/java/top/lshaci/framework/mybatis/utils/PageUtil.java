package top.lshaci.framework.mybatis.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.lshaci.framework.common.model.PageQuery;
import top.lshaci.framework.common.model.PageResult;

/**
 * PageUtil
 *
 * @author lshaci
 * @since 1.0.7
 */
public class PageUtil {

    /**
     * 根据PageQuery获取mybatis plus分页查询对象
     *
     * @param pageQuery page query
     * @return page
     */
    public static Page page(PageQuery pageQuery) {
        return new Page(pageQuery.getPgCt(), pageQuery.getPgSz());
    }

    /**
     * 根据mybatis plus分页结果构建PageResult
     *
     * @param page 分页数据
     * @param <T> 数据类型
     * @return PageResult
     */
    public static <T> PageResult<T> pageResult(IPage<T> page) {
        return new PageResult<>((int) page.getCurrent(), (int) page.getSize(), (int) page.getTotal(), page.getRecords());
    }
}
