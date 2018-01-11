package top.lshaci.framework.excel.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import lombok.Getter;
import lombok.Setter;

/**
 * The excel relation model
 * 
 * @author lshaci
 * @since 0.0.3
 */
@Getter
@Setter
public class ExcelRelationModel {

	private Field targetField;	// the entity target field
	private Object convertInstance; // the convert instance
	private Method convertMethod; // the convert method
	
	/**
	 * Construction a new instance by target field
	 * 
	 * @param targetField the entity target field
	 */
	public ExcelRelationModel(Field targetField) {
		super();
		this.targetField = targetField;
	}
	
	
}
