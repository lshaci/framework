package top.lshaci.framework.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * LabelValueVO
 *
 * @author lshaci
 * @since 1.0.7
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class LabelValueVO {

    /**
     * 标签
     */
    private String label;

    /**
     * 值
     */
    private Object value;
}
