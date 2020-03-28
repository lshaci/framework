package top.lshaci.framework.web.service.impl;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import top.lshaci.framework.web.service.PreventRepeat;

/**
 * <p>Time cache prevent repeat submit</p><br>
 *
 * <b>1.0.7:</b>This method <code>getAndSet</code> add parameter <code>timeout</code>
 *
 * @author lshaci
 * @since 1.0.5
 * @version 1.0.7
 */
public class TimedCachePreventRepeat implements PreventRepeat {

    /**
     * The timeout of the submit key
     */
    private final long timeout;

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
        this.timeout = timeout;
        timedCache = CacheUtil.newTimedCache(timeout);
        timedCache.schedulePrune(10);
    }

    @Override
    public String getAndSet(String key, long timeout) {
        String value = timedCache.get(key, false);
        timedCache.put(key, VALUE, timeout > 0 ? timeout : this.timeout);
        return value;
    }

    @Override
    public void remove(String key) {
        timedCache.remove(key);
    }
}
