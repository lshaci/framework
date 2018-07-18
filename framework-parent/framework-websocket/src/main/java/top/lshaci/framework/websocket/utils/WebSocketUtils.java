package top.lshaci.framework.websocket.utils;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.Session;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.common.exception.BaseException;

/**
 * Web socket util
 * 
 * @author lshaci
 * @since 0.0.4
 */
@Slf4j
public class WebSocketUtils {

	/**
	 * The web socket session cache map
	 */
	private static Map<String, Session> sessionMap = new ConcurrentHashMap<>();
	
	/**
	 * Add web socket session
	 * 
	 * @param key unique key of the session
	 * @param session the web socket session
	 */
	public static void add(String key, Session session) {
		validateParameter(key, session);
		sessionMap.put(key, session);
		log.info("Add web socket session[{}] to the sessionMap success!", key);
	}
	
	/**
	 * Remove web socket session
	 * 
	 * @param key unique key of the session
	 */
	public static void remove(String key) {
		validateKey(key);
		boolean containsKey = sessionMap.containsKey(key);
		if (containsKey) {
			sessionMap.remove(key);
			log.info("Remove web socket session[{}] from sessionMap success!", key);
		} else {
			log.warn("The key[{}] not included in the sessionMap!", key);
		}
	}
	
	/**
	 * Send json message
	 * 
	 * @param key unique key of the session
	 * @param message the message need to be sent(<b>This message will be converted to JSON string</b>)
	 */
	public static void sendMsg(String key, Object message) {
		validateKey(key);
		Objects.requireNonNull(message, "The message must not be null!");
		Session session = sessionMap.get(key);
		if (session == null) {
			throw new BaseException("The key of the session not exist!");
		}
		try {
			session.getBasicRemote().sendText(JSON.toJSONString(message));
		} catch (IOException e) {
			log.error("Sending message failed!", e);
			throw new BaseException("Sending message failed!");
		}
	}
	
	/**
	 * Validate parameters
	 * 
	 * @param key unique key of the session
	 * @param session the web socket session
	 */
	private static void validateParameter(String key, Session session) {
		validateKey(key);
		Objects.requireNonNull(session, "The web socket session must not be null!");
	}
	
	/**
	 * Validate unique key of the session
	 * 
	 * @param key unique key of the session
	 */
	private static void validateKey(String key) {
		if (StringUtils.isBlank(key)) {
			throw new BaseException("The unique key of the session must not be empty!");
		}
	}
	
}
