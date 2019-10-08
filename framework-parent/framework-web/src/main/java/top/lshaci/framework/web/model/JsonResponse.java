package top.lshaci.framework.web.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Web controller json response<br><br>
 *
 * <b>0.0.4:</b> Add method: successMessage and message; Change the name of the params field to otherData
 *
 * @author lshaci
 * @since 0.0.1
 * @version 0.0.4
 */
@Data
@Accessors(chain = true)
public class JsonResponse<R> implements Serializable {

    private static final long serialVersionUID = 7574078101944305355L;

    /**
     * 状态信息; true: 成功, false: 失败
     */
    private boolean status;

    /**
     * 响应码
     */
    private int code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 需要返回的数据
     */
    private R data;

    /**
     * 返回添加的额外数据
     */
    private Map<String, Object> otherData = new HashMap<>();

    /**
     * Build a json response
     *
     * @return json response
     */
    public static <R> JsonResponse<R> build() {
    	return new JsonResponse<>();
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


}
