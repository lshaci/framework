package top.lshaci.framework.core.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Web controller json response
 *
 * @author lshaci
 * @version 0.0.1
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

    public JsonResponse() {
    }

    /**
     * Create success response
     *
     * @param data The response data
     */
    public JsonResponse(Object data) {
        if (data != null) {
            this.status = true;
            this.data = data;
        }
    }

    /**
     * Create a response
     *
     * @param status	The response status
     * @param data	 	The response data
     */
    public JsonResponse(boolean status, Object data) {
        super();
        this.status = status;
        this.data = data;
    }

    /**
     * Create a response
     *
     * @param status	The response status
     * @param message	 The response message
     */
    public JsonResponse(boolean status, String message) {
        super();
        this.status = status;
        this.message = message;
    }

    /**
     * Create a response
     *
     * @param status	The response status
     * @param message	The response message
     * @param data		The response data
     */
    public JsonResponse(boolean status, String message, Object data) {
        super();
        this.status = status;
        this.message = message;
        this.data = data;
    }

    /**
     * Create a response
     *
     * @param status	The response status
     * @param code      The response code
     * @param message	The response message
     * @param data		The response data
     */
    public JsonResponse(boolean status, int code, String message, Object data) {
        super();
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
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
     * Add other data
     *
     * @param key	key of data
     * @param value	data
     * @return this
     */
    public JsonResponse addParam(String key, Object value) {
        this.params.put(key, value);
        return this;
    }

    /**
     * Remove other data
     *
     * @param key	key of data
     * @return this
     */
    public JsonResponse removeParam(String key) {
        this.params.remove(key);
        return this;
    }

}
