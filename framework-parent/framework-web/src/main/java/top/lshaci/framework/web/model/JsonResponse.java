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
     * Constructs a success json response with data
     *
     * @param data the response data
     */
    public JsonResponse(Object data) {
        if (data != null) {
            this.status = true;
            this.data = data;
        }
    }

    /**
     * Constructs a json response with status and data
     *
     * @param status the response status
     * @param data the response data
     */
    public JsonResponse(boolean status, Object data) {
        super();
        this.status = status;
        this.data = data;
    }

    /**
     * Constructs a json response with status and message
     *
     * @param status the response status
     * @param message the response message
     */
    public JsonResponse(boolean status, String message) {
        super();
        this.status = status;
        this.message = message;
    }

    /**
     * Constructs a json response with status, message, data
     *
     * @param status the response status
     * @param message the response message
     * @param data the response data
     */
    public JsonResponse(boolean status, String message, Object data) {
        this(status, message);
        this.data = data;
    }

    /**
     * Constructs a json response with status, code, message, data
     *
     * @param status the response status
     * @param code the response code
     * @param message the response message
     * @param data the response data
     */
    public JsonResponse(boolean status, int code, String message, Object data) {
        this(status, message, data);
        this.code = code;
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

}
