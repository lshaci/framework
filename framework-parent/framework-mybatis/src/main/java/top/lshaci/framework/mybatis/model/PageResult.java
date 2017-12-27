package top.lshaci.framework.mybatis.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.github.pagehelper.Page;

import top.lshaci.framework.common.constants.Constants;

/**
 * Page query result
 * 
 * @author lshaci
 * @param <T>	The result entity type
 * @version 0.0.1
 */
public class PageResult<T> implements Serializable {
	
	private static final long serialVersionUID = 2696109518770817050L;
	
	private int pgCt;
	private int pgSz;
	private int end;
	private int total;
	private List<T> datas = new ArrayList<>();
	
	
	/**
	 * Constructs a new page result with the current page number and page size
	 * 
	 * @param pgCt the current page number
	 * @param pgSz the page size
	 */
	public PageResult(int pgCt, int pgSz) {
		this(pgCt, pgSz, 0);
	}

	/**
	 * Constructs a new page result with the current page number, page size, data total
	 *
	 * @param pgCt the current page number
	 * @param pgSz the page size
	 * @param total the data total
	 */
	public PageResult(int pgCt, int pgSz, int total) {
		this.pgCt = pgCt < 1 ? Constants.DEFAULT_PGCT : pgCt;
		this.pgSz = pgSz < 1 ? Constants.DEFAULT_PGSZ : pgSz;
		this.total = total;
		this.end = (total + pgSz - 1) / pgSz;
	}

	/**
	 * Constructs a new page result with the current page number, page size, data total, datas
	 *
	 * @param pgCt the current page number
	 * @param pgSz the page size
	 * @param total the data total
	 * @param datas the datas
	 */
	public PageResult(int pgCt, int pgSz, int total, List<T> datas) {
		this(pgCt, pgSz, total);
		this.datas = datas;
	}

	/**
	 * Constructs a new page result with the page helper result
	 * 
	 * @param page the page helper result
	 */
	public PageResult(Page<T> page) {
		Objects.requireNonNull(page, "Page result must not be null!");
		
		this.pgCt = page.getPageNum();
		this.pgSz = page.getPageSize();
		this.total = (int) page.getTotal();
		this.datas = page.getResult();
		this.end = page.getPages();
	}


	/**
	 * Get the current page number
	 *
	 * @return the current page number
	 */
	public int getPgCt() {
		return pgCt;
	}

	/**
	 * Get the page size
	 *
	 * @return the page size
	 */
	public int getPgSz() {
		return pgSz;
	}

	/**
	 * Get the total pages
	 *
	 * @return the total pages
	 */
	public int getEnd() {
		return end;
	}

	/**
	 * Get the data total
	 *
	 * @return the data total
	 */
	public long getTotal() {
		return total;
	}

	/**
	 * Get the datas
	 *
	 * @return the datas
	 */
	public List<T> getDatas() {
		return datas;
	}

	/**
	 * Set the datas
	 *
	 * @param datas the data list
	 */
	public void setDatas(List<T> datas) {
		this.datas = datas;
	}
}
