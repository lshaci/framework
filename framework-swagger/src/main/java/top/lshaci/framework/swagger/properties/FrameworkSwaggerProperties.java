package top.lshaci.framework.swagger.properties;

import com.google.common.base.Predicate;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.CollectionUtils;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.Parameter;
import top.lshaci.framework.swagger.model.Contact;
import top.lshaci.framework.swagger.model.DocketInfo;
import top.lshaci.framework.swagger.model.GlobalParameter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * <p>Swagger 2 config properties</p><br>
 * <b>1.0.1: </b>添加分组配置<br>
 * <b>1.0.2: </b>修改swagger配置属性前缀<br>
 * <b>1.0.7: </b>将内部类提取到model包中
 *
 * @author lshaci
 * @since 0.0.4
 * @version 1.0.7
 */
@Data
@ConfigurationProperties(prefix = FrameworkSwaggerProperties.SWAGGER_PREFIX)
public class FrameworkSwaggerProperties {

	/**
	 * The swagger properties prefix
	 */
	public final static String SWAGGER_PREFIX = "framework.swagger";

	/**
	 * 是否开启swagger配置(默认开启)
	 */
	private Boolean enabled = true;

	/**
	 * The controller base package
	 */
	private String basePackage;
	/**
	 * The swagger api title
	 */
	private String title = "Swagger2 RESTful APIs";
	/**
	 * The swagger api description
	 */
	private String description = "Spring boot Project Use Swaggers UI Build RESTful APIs";
	/**
	 * The swagger api version
	 */
	private String version = "1.0";
	/**
	 * The swagger api license
	 */
	private String license;
	/**
	 * The swagger api license url
	 */
	private String licenseUrl;
	/**
	 * The swagger api terms of service url
	 */
	private String termsOfServiceUrl;
	/**
	 * The swagger api
	 */
	private String host;
    /**
     * Url rules that need to be parsed
     */
    private List<String> basePath = new ArrayList<>();
    /**
     * Url rules that need to be excluded
     */
    private List<String> excludePath = new ArrayList<>();

	/**
	 * The swagger api maintenance personnel
	 */
	private Contact contact = new Contact();

	/**
	 * Group information
	 */
    private Map<String, DocketInfo> docket = new LinkedHashMap<>();

	/**
	 * 全局参数
	 */
	private List<GlobalParameter> globalParameters = new ArrayList<>();

	/**
	 * 获取全局参数
	 *
	 * @return 全局参数
	 */
	public List<Parameter> globalParameter() {
		return globalParameters.stream().map(GlobalParameter::build).collect(toList());
	}

	/**
	 * 需要匹配的路径
	 */
	public List<Predicate<String>> basePath() {
		if (basePath.isEmpty()) {
			basePath.add("/**");
		}
		return basePath.stream().map(PathSelectors::ant).collect(toList());
	}

	/**
	 * 不需要匹配的路径
	 */
	public List<Predicate<String>> excludePath() {
		return excludePath.stream().map(PathSelectors::ant).collect(toList());
	}
}
