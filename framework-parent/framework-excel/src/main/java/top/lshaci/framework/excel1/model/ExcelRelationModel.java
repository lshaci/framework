package top.lshaci.framework.excel1.model;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * The excel1 relation model
 *
 * @author lshaci
 * @since 0.0.3
 * @version 0.0.4
 */
@Getter
@Setter
public class ExcelRelationModel {

	private Field targetField;	// the entity target field
	private Object convertInstance; // the convert instance
	private Method convertMethod; // the convert method
	private boolean require; // the field is require

	/**
	 * Construction a new instance by target field
	 *
	 * @param targetField the entity target field
	 */
	public ExcelRelationModel(Field targetField) {
		this.targetField = targetField;
	}


}
