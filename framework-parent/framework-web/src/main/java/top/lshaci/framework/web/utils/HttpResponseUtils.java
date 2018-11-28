package top.lshaci.framework.web.utils;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;

import top.lshaci.framework.web.enums.ContentType;
import top.lshaci.framework.web.enums.Encoding;
import top.lshaci.framework.web.model.JsonResponse;

/**
 * Http response utils
 * 
 * @author lshaci
 * @since 0.0.4
 */
public class HttpResponseUtils {

	/**
	 * Get the http servlet response
	 * 
	 * @return the http servlet response
	 */
	public static HttpServletResponse getResponse() {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		
		HttpServletResponse response = requestAttributes.getResponse();
		Objects.requireNonNull(response, "The http response is null!");
		
		return response;
	}
	
	/**
	 * Response json use the http servlet response
	 * 
	 * @param jsonResponse the json response
	 * @throws IOException 
	 */
	public static <R> void responseJson(JsonResponse<R> jsonResponse) throws IOException {
		HttpServletResponse response = getResponse();
		
		response.setCharacterEncoding(Encoding.UTF_8.getName());
		response.setContentType(ContentType.JSON_UTF_8.getName());
		response.getWriter().write(JSON.toJSONString(jsonResponse));
	}
	
}
