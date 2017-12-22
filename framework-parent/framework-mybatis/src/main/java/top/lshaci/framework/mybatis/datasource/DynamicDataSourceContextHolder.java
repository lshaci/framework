package top.lshaci.framework.mybatis.datasource;

/**
 * Dynamic DataSource Context Holder
 * 
 * @author lshaci
 * @version 0.0.1
 */
public class DynamicDataSourceContextHolder {

	private static final ThreadLocal<DynamicDataSourceType> contextHolder = new ThreadLocal<>();

	/**
	 * Set data source type
	 * 
	 * @param dataSourceType	the data source type
	 */
	public static void setDataSourceType(DynamicDataSourceType dataSourceType) {
		contextHolder.set(dataSourceType);
	}

	/**
	 * Get data source type
	 * 
	 * @return the data source type
	 */
	public static DynamicDataSourceType getDataSourceType() {
		return contextHolder.get();
	}

	/**
	 * Clear data source typ
	 */
	public static void clearDataSourceType() {
		contextHolder.remove();
	}

}
