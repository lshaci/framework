package top.lshaci.framework.websocket.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import lombok.extern.slf4j.Slf4j;

/**
 * Web socket config
 * 
 * @author lshaci
 * @since 0.0.4
 */
@Slf4j
@Configuration
public class WebsocketConfig {

	/**
	 * Config server endpoint exporter
	 * 
	 * @return the server endpoint exporter bean
	 */
	@Bean
	@ConditionalOnProperty(value = "websocket.enabled", havingValue = "true", matchIfMissing = false)
	public ServerEndpointExporter serverEndpointExporter() {
		log.debug("Init server endpoint exporter...");
		return new ServerEndpointExporter();
	}
}
