package top.lshaci.framework.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.web.utils.DownloadUtils;

/**
 * Common config
 * 
 * @author lshaci
 * @since 0.0.4
 * @version 0.0.4
 */
@Slf4j
@Configuration
public class CommonConfig {

    @Value("${dowanload.cacheSize}")
    private int downloadCacheSize;

    @Autowired
    public void setDownloadCacheSize() {
        if (this.downloadCacheSize <= 0) {
            this.downloadCacheSize = 2048;
        }
        log.info("Set DownloadUtils cacheSize: {} bytes", this.downloadCacheSize);
        DownloadUtils.cacheSize = this.downloadCacheSize;
    }
}
