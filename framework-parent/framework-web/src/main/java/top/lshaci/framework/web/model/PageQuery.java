package top.lshaci.framework.web.model;

import lombok.Getter;
import lombok.ToString;
import top.lshaci.framework.common.constants.Constants;

import java.io.Serializable;

/**
 * <p>Page base query entity</p><br>
 *
 * <b>0.0.4:</b> Change to non-abstract<br>
 * <b>1.0.3:</b> 添加{code @ToString}
 *
 * @author lshaci
 * @since 0.0.1
 * @version 1.0.3
 */
@Getter
@ToString
public class PageQuery implements Serializable {

    private static final long serialVersionUID = 2987113271659158089L;

    private Integer pgCt = Constants.DEFAULT_PGCT;
    private Integer pgSz = Constants.DEFAULT_PGSZ;

    /**
     * Set current page number
     *
     * @param pgCt the current page number
     */
    public void setPgCt(Integer pgCt) {
        this.pgCt = pgCt == null || pgCt < 0 ? Constants.DEFAULT_PGCT : pgCt;
    }

    /**
     * Set page size
     *
     * @param pgSz the page size
     */
    public void setPgSz(Integer pgSz) {
        this.pgSz = pgSz == null || pgSz < 0 ? Constants.DEFAULT_PGSZ : pgSz;
    }

    /**
     * Get the start row
     *
     * @return the start row
     */
    public Integer getStart() {
        return (this.pgCt - 1) * this.pgSz;
    }

}
