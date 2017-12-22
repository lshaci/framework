package top.lshaci.framework.core.model;

import lombok.Getter;
import top.lshaci.framework.core.constants.Constants;

import java.io.Serializable;

/**
 * Page base query entity
 *
 * @author lshaci
 * @version 0.0.1
 */
@Getter
public abstract class PageQuery implements Serializable {

    private static final long serialVersionUID = 2987113271659158089L;

    private int pgCt = Constants.DEFAULT_PGCT;
    private int pgSz = Constants.DEFAULT_PGSZ;

    private String keyword;    // the key word query condition

    /**
     * Set current page number
     *
     * @param pgCt the current page number
     */
    public void setPgCt(int pgCt) {
        this.pgCt = pgCt > 0 ? pgCt : Constants.DEFAULT_PGCT;
    }

    /**
     * Set page size
     *
     * @param pgSz the page size
     */
    public void setPgSz(int pgSz) {
        this.pgSz = pgSz > 0 ? pgSz : Constants.DEFAULT_PGSZ;
    }

    /**
     * Get the start row
     *
     * @return the start row
     */
    public int getStart() {
        return (this.pgCt - 1) * this.pgSz;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
