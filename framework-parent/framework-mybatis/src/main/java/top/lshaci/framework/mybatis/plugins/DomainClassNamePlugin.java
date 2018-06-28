package top.lshaci.framework.mybatis.plugins;

import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;

/**
 * Domain class name plugin with mybatis generator
 * 
 * @author lshaci
 * @since 0.0.3
 */
public class DomainClassNamePlugin extends PluginAdapter {
	
	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}

	@Override
	public void initialized(IntrospectedTable table) {
		super.initialized(table);
		
		String domainName = table.getBaseRecordType();
		String temp = domainName.substring(domainName.lastIndexOf("."), domainName.length());
		if (temp.startsWith(".T")) {
			String finalName = domainName.replace(".T", ".");
			table.setBaseRecordType(finalName);
		}
		
		String mapperName = table.getMyBatis3JavaMapperType();
		table.setMyBatis3JavaMapperType(mapperName.replace(".T", "."));
		
		String mapperXmlName = table.getMyBatis3XmlMapperFileName();
		table.setMyBatis3XmlMapperFileName(mapperXmlName.replace(".T", "."));
	}
}
