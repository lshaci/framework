package top.lshaci.framework.web.model;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import top.lshaci.framework.common.constants.Constants;

/**
 * Page base query entity<br><br>
 * 
 * <b>0.0.4:</b> Change to non-abstract
 *
 * @author lshaci
 * @since 0.0.1
 * @version 0.0.4
 */
@Getter
public class PageQuery implements Serializable {

    private static final long serialVersionUID = 2987113271659158089L;

    private Integer pgCt = Constants.DEFAULT_PGCT;
    private Integer pgSz = Constants.DEFAULT_PGSZ;

    private String keyword;    // the key word query condition

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
