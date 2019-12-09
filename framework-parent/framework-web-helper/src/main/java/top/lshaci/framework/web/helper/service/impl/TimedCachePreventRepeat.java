package top.lshaci.framework.web.helper.service.impl;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import top.lshaci.framework.web.helper.service.PreventRepeat;

/**
 * Time cache prevent repeat submit
 *
 * @author lshaci
 * @since 1.0.5
 */
public class TimedCachePreventRepeat implements PreventRepeat {

    /**
     * Timed cache
     */
    private final TimedCache<String, String> timedCache;

    /**
     * Constructor a timed cache prevent repeat with the timeout
     *
     * @param timeout This timeout of the submit key
     */
    public TimedCachePreventRepeat(long timeout) {
        timedCache = CacheUtil.newTimedCache(timeout);
        timedCache.schedulePrune(10);
    }

    @Override
    public String getAndSet(String key) {
        String value = timedCache.get(key, false);
        timedCache.put(key, VALUE);
        return value;
    }

    @Override
    public void remove(String key) {
        timedCache.remove(key);
    }
}
