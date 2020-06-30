package top.lshaci.framework.swagger.model;

import com.google.common.base.Predicate;
import lombok.Data;
import springfox.documentation.builders.PathSelectors;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Group docket information
 *
 * @author lshaci
 * @since 1.0.7
 */
@Data
public class DocketInfo {
    /**
     * The controller base package
     */
    private String basePackage;
    /**
     * The swagger api title
     */
    private String title = "";
    /**
     * The swagger api description
     */
    private String description = "";
    /**
     * The swagger api version
     */
    private String version = "";
    /**
     * The swagger api license
     */
    private String license = "";
    /**
     * The swagger api license url
     */
    private String licenseUrl = "";
    /**
     * The swagger api terms of service url
     */
    private String termsOfServiceUrl = "";
    /**
     * The swagger api
     */
    private String host = "";
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
