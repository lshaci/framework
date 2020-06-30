package top.lshaci.framework.excel.builder;

/**
 * 序号列顺序构建者
 *
 * @author lshaci
 * @since 1.0.2
 */
public interface IndexBuilder {

    /**
     * 获取序号
     *
     * @return 序号字符串
     */
    String get();

    /**
     * 重置序号
     */
    void reset();
}
