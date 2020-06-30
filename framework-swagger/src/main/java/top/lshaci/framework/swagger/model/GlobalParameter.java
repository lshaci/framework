package top.lshaci.framework.swagger.model;

import lombok.Data;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;

/**
 * GlobalParameter
 *
 * @author lshaci
 * @since 1.0.7
 */
@Data
public class GlobalParameter {
    /**
     * 参数名称
     */
    private String name;
    /**
     * 参数描述
     */
    private String description;
    /**
     * 参数引用类型
     */
    private String modelRef;
    /**
     * 参数类型(query,header)
     */
    private String type;
    /**
     * 是否必须
     */
    private boolean required = false;

    /**
     * 构建参数
     *
     * @return 全局参数
     */
    public Parameter build() {
        return new ParameterBuilder()
                .name(name)
                .description(description)
                .modelRef(new ModelRef(modelRef))
                .parameterType(type)
                .required(required)
                .build();
    }

}
