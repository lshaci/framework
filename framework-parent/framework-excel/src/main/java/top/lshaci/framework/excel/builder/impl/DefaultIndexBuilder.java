package top.lshaci.framework.excel.builder.impl;

import top.lshaci.framework.excel.builder.IndexBuilder;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 默认的序号构建者
 *
 * @author lshaci
 * @since 1.0.2
 */
public class DefaultIndexBuilder implements IndexBuilder {

    private AtomicInteger atomicInteger;

    public DefaultIndexBuilder() {
        atomicInteger = new AtomicInteger(1);
    }

    @Override
    public String get() {
        return atomicInteger.getAndIncrement() + "";
    }

    @Override
    public void reset() {
        atomicInteger = new AtomicInteger(1);
    }
}
