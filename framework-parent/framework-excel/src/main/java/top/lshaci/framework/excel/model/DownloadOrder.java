package top.lshaci.framework.excel.model;

import java.lang.reflect.Field;

import lombok.Data;

/**
 * The excel download title order
 * 
 * @author lshaci
 * @since 0.0.3
 */
@Data
public class DownloadOrder implements Comparable<DownloadOrder> {

	private int order;
	private String title;
	private Field field;
	
	@Override
	public int compareTo(DownloadOrder downloadOrder) {
		return this.order - downloadOrder.getOrder();
	}
}
