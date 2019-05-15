package top.lshaci.framework.excel1.model;

import lombok.Data;

import java.lang.reflect.Field;

/**
 * The excel1 download title order
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

	public DownloadOrder(Field field) {
		super();
		this.field = field;
	}

	@Override
	public int compareTo(DownloadOrder downloadOrder) {
		return this.order - downloadOrder.getOrder();
	}

}
