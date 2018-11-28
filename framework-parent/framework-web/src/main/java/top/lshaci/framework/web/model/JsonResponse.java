package top.lshaci.framework.web.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

/**
 * Web controller json response<br><br>
 * 
 * <b>0.0.4:</b> Add method: successMessage and message; Change the name of the params field to otherData
 *
 * @author lshaci
 * @since 0.0.1
 * @version 0.0.4
 */
@Getter
public class JsonResponse<R> implements Serializable {

    private static final long serialVersionUID = 7574078101944305355L;

    private boolean status;

    private int code;

    private String message;

    private R data;

    private Map<String, Object> otherData = new HashMap<>();

    /**
     * Constructor a json response
     */
    public JsonResponse() {
    }
    
    /**
     * Build a success json response with data
     *
     * @param data the success response data
     * @return success json response
     */
    public static <R> JsonResponse<R> success(R data) {
        JsonResponse<R> response = new JsonResponse<>();
        response.status = true;
        response.data = data;
        return response;
    }
    
    /**
     * Build a success json response with message
     *
     * @param message the success response message
     * @return success json response
     */
    public static <R> JsonResponse<R> successMessage(String message) {
        return message(true, message);
    }
    
    /**
     * Build a message json response with status and message
     *
     * @param status the response status
     * @param message the response message
     * @return message json response
     */
    public static <R> JsonResponse<R> message(boolean status, String message) {
        JsonResponse<R> response = new JsonResponse<>();
        response.status = status;
        response.message = message;
        return response;
    }
    
    /**
     * Build a failure json response with message
     *
     * @param message the failure response message
     * @return failure json response
     */
    public static <R> JsonResponse<R> failure(String message) {
        return message(false, message);
    }

    /**
     * Set other data
     *
     * @param otherData other data
     * @return this
     */
    public JsonResponse<R> setOtherData(Map<String, Object> otherData) {
        this.otherData = otherData;
        return this;
    }

    /**
     * Add other data value
     *
     * @param key key of other data
     * @param value value of other data
     * @return this
     */
    public JsonResponse<R> addOtherData(String key, Object value) {
        this.otherData.put(key, value);
        return this;
    }

    /**
     * Remove other data value
     *
     * @param key key of other data
     * @return this
     */
    public JsonResponse<R> removeOtherData(String key) {
        this.otherData.remove(key);
        return this;
    }

    /**
     * Set response status
     *
     * @param status json response status
     * @return this
     */
    public JsonResponse<R> setStatus(boolean status) {
        this.status = status;
        return this;
    }

    /**
     * Set response code
     *
     * @param code json response code
     * @return this
     */
    public JsonResponse<R> setCode(int code) {
        this.code = code;
        return this;
    }

    /**
     * Set response message
     *
     * @param message json response message
     * @return this
     */
    public JsonResponse<R> setMessage(String message) {
        this.message = message;
        return this;
    }

    /**
     * Set response data
     *
     * @param data json response data
     * @return this
     */
    public JsonResponse<R> setData(R data) {
        this.data = data;
        return this;
    }
    
}
