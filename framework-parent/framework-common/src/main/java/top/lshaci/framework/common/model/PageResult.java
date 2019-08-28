package top.lshaci.framework.common.model;

import lombok.Data;
import top.lshaci.framework.common.constants.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Page qo result
 *
 * @author lshaci
 * @param <T>	The result entity type
 * @since 0.0.1
 */
@Data
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
		this.end = (total + this.pgSz - 1) / this.pgSz;
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

}
