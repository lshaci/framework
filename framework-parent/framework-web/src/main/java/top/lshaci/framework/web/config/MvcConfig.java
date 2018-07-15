package top.lshaci.framework.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.web.utils.DownloadUtils;

/**
 * Spring web mvc config<br><br>
 * 
 * <b>0.0.4:</b> Add setDownloadCacheSize method
 * 
 * @author lshaci
 * @since 0.0.3
 * @version 0.0.4
 */
@Slf4j
@Configuration
public class MvcConfig {
    
    @Value("${dowanload.cacheSize}")
    private int downloadCacheSize;

    /**
     * Config request context listener
     * 
     * @return the request context listener bean
     */
    @Bean
    public RequestContextListener requestContextListener() {
        log.debug("Config request context listener...");
        return new RequestContextListener();
    }
    

    /**
     * Set download utils cache size
     */
    @Autowired
    public void setDownloadCacheSize() {
        if (this.downloadCacheSize <= 0) {
            this.downloadCacheSize = 2048;
        }
        log.debug("Set DownloadUtils cacheSize: {} bytes", this.downloadCacheSize);
        DownloadUtils.cacheSize = this.downloadCacheSize;
    }
    
}
