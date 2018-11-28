package top.lshaci.framework.mybatis.model;

import java.util.List;

import com.github.pagehelper.Page;

import lombok.Getter;
import lombok.Setter;
import top.lshaci.framework.common.model.PageResult;

/**
 * Page query result
 * 
 * @author lshaci
 * @param <T>	The result entity type
 * @since 0.0.4
 */
@Setter
@Getter
public class MybatisPageResult<T> extends PageResult<T> {
	
    private static final long serialVersionUID = 3497792361815572368L;

    /**
	 * Constructs a new page result with the current page number and page size
	 * 
	 * @param pgCt the current page number
	 * @param pgSz the page size
	 */
	public MybatisPageResult(int pgCt, int pgSz) {
		super(pgCt, pgSz, 0);
	}

	/**
	 * Constructs a new page result with the current page number, page size, data total
	 *
	 * @param pgCt the current page number
	 * @param pgSz the page size
	 * @param total the data total
	 */
	public MybatisPageResult(int pgCt, int pgSz, int total) {
	    super(pgCt, pgSz, total);
	}

	/**
	 * Constructs a new page result with the current page number, page size, data total, datas
	 *
	 * @param pgCt the current page number
	 * @param pgSz the page size
	 * @param total the data total
	 * @param datas the datas
	 */
	public MybatisPageResult(int pgCt, int pgSz, int total, List<T> datas) {
	    super(pgCt, pgSz, total, datas);
	}

	/**
	 * Constructs a new page result with the page helper result
	 * 
	 * @param page the page helper result
	 */
	public MybatisPageResult(Page<T> page) {
	    this(page.getPageNum(), page.getPageSize(), (int) page.getTotal(), page.getResult());
	}

}
