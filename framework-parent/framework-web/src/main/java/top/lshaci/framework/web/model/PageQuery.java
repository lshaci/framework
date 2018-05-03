package top.lshaci.framework.web.model;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import top.lshaci.framework.common.constants.Constants;

/**
 * Page base query entity<br><br>
 * 
 * <b>0.0.4:</b> Delete the abstract definition of class.
 *
 * @author lshaci
 * @since 0.0.1
 * @version 0.0.4
 */
@Getter
public class PageQuery implements Serializable {

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

    /**
     * Set key word query condition
     * 
     * @param keyword the key word
     */
    public void setKeyword(String keyword) {
    	if (StringUtils.isNotBlank(keyword)) {
    		this.keyword = keyword;
		}
    }
}
