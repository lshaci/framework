package top.lshaci.framework.web.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.lshaci.framework.common.model.JsonResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

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
	public static HttpServletResponse get() {
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
		HttpServletResponse response = get();

		response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		response.getWriter().write(JSON.toJSONString(jsonResponse));
	}

}
