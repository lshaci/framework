package top.lshaci.framework.excel.model;

import java.lang.reflect.Field;

import lombok.Data;

/**
 * The excel download title order
 * 
 * @author lshaci
 * @since 0.0.4
 */
@Data
public class DownloadOrder implements Comparable<DownloadOrder> {

	private int order;
	private String title;
	private Field field;
	private int columnWidth;
	
	@Override
	public int compareTo(DownloadOrder downloadOrder) {
		return this.order - downloadOrder.getOrder();
	}
}
