package top.lshaci.framework.web.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Web controller json response
 *
 * @author lshaci
 * @since 0.0.1
 * @version 0.0.4
 */
@Getter
@Setter
public class JsonResponse implements Serializable {

    private static final long serialVersionUID = 7574078101944305355L;

    private boolean status;

    private int code;

    private String message;

    private Object data;

    private Map<String, Object> params = new HashMap<>();

    /**
     * Privatized constructor
     */
    private JsonResponse() {
    }
    
    /**
     * Build a success json response with data
     *
     * @param data the success response data
     * @return success json response
     */
    public static JsonResponse build() {
    	return new JsonResponse();
    }
    
    /**
     * Build a success json response with data
     *
     * @param data the success response data
     * @return success json response
     */
    public static JsonResponse success(Object data) {
    	JsonResponse response = build();
    	if (data != null) {
    		response.status = true;
    		response.data = data;
        } else {
        	response.status = false;
        }
    	return response;
    }
    
    /**
     * Build a failure json response with data
     *
     * @param message the failure response message
     * @return failure json response
     */
    public static JsonResponse failure(String message) {
    	JsonResponse response = build();
		response.status = false;
		response.message = message;
    	return response;
    }

    /**
     * Set other datas
     *
     * @param params datas
     * @return this
     */
    public JsonResponse setParams(Map<String, Object> params) {
        this.params = params;
        return this;
    }

    /**
     * Add other data value
     *
     * @param key	key of  other data
     * @param value	value of  other data
     * @return this
     */
    public JsonResponse addParam(String key, Object value) {
        this.params.put(key, value);
        return this;
    }

    /**
     * Remove other data value
     *
     * @param key	key of  other data
     * @return this
     */
    public JsonResponse removeParam(String key) {
        this.params.remove(key);
        return this;
    }

	/**
	 * Set response status
	 *
	 * @param status json response status
	 * @return this
	 */
	public JsonResponse setStatus(boolean status) {
		this.status = status;
		return this;
	}

	/**
	 * Set response code
	 *
	 * @param code json response code
	 * @return this
	 */
	public JsonResponse setCode(int code) {
		this.code = code;
		return this;
	}

	/**
	 * Set response message
	 *
	 * @param message json response message
	 * @return this
	 */
	public JsonResponse setMessage(String message) {
		this.message = message;
		return this;
	}

	/**
	 * Set response data
	 *
	 * @param data json response data
	 * @return this
	 */
	public JsonResponse setData(Object data) {
		this.data = data;
		return this;
	}

}
