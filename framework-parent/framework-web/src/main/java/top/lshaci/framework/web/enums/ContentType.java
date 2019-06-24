package top.lshaci.framework.web.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Content type enums
 *
 * @author lshaci
 * @since 0.0.4
 */
@Getter
@AllArgsConstructor
public enum ContentType {

	/**
	 * application/json; charset=utf-8
	 */
	JSON_UTF_8("application/json; charset=utf-8"),
	;

	private String name;

}
