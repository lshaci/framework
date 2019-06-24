package top.lshaci.framework.web.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Character encoding enums
 *
 * @author lshaci
 * @since 0.0.4
 */
@Getter
@AllArgsConstructor
public enum Encoding {

	/**
	 * UTF-8
	 */
	UTF_8("UTF-8"),
	;

	private String name;

}
