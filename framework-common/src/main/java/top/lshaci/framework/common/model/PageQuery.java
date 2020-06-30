package top.lshaci.framework.common.model;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

import static top.lshaci.framework.common.constants.Constants.DEFAULT_PGCT;
import static top.lshaci.framework.common.constants.Constants.DEFAULT_PGSZ;

/**
 * <p>Page base query entity</p><br>
 *
 * <b>0.0.4: </b> Change to non-abstract<br>
 * <b>1.0.3: </b> 添加{code @ToString}<br>
 * <b>1.0.5: </b> Move form framework web module to framework common module <br>
 *
 * @author lshaci
 * @since 0.0.1
 * @version 1.0.5
 */
@Getter
@ToString
public class PageQuery implements Serializable {

    private static final long serialVersionUID = 2987113271659158089L;

    private Integer pgCt = DEFAULT_PGCT;
    private Integer pgSz = DEFAULT_PGSZ;

    /**
     * Set current page number
     *
     * @param pgCt the current page number
     */
    public void setPgCt(Integer pgCt) {
        this.pgCt = pgCt == null || pgCt <= 0 ? DEFAULT_PGCT : pgCt;
    }

    /**
     * Set page size
     *
     * @param pgSz the page size
     */
    public void setPgSz(Integer pgSz) {
        this.pgSz = pgSz == null || pgSz <= 0 ? DEFAULT_PGSZ : pgSz;
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
