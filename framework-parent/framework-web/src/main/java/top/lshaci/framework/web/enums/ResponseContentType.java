package top.lshaci.framework.web.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Http servlet response content type
 *
 * @author lshaci
 * @since 0.0.4
 */
@Getter
@AllArgsConstructor
public enum ResponseContentType {

    /**
     * application/vnd.ms-excel;charset=utf-8
     */
     EXCEL_CONTENT_TYPE		("application/vnd.ms-excel;charset=utf-8"),
    /**
     * Http servlet response pdf content type
     */
     PDF_CONTENT_TYPE		("application/pdf;charset=utf-8");
	;

	private String name;
}
