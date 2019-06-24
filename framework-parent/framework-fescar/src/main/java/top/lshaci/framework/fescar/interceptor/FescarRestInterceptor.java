package top.lshaci.framework.fescar.interceptor;

import java.io.IOException;
import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import com.alibaba.fescar.core.context.RootContext;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import top.lshaci.framework.fescar.properties.FescarProperties;

public class FescarRestInterceptor implements RequestInterceptor, ClientHttpRequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String xid = RootContext.getXID();
        if(StringUtils.isNotBlank(xid)){
            requestTemplate.header(FescarProperties.FESCAR_XID, xid);
        }
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String xid = RootContext.getXID();
        if(StringUtils.isNotBlank(xid)){
            HttpHeaders headers = request.getHeaders();
            headers.put(FescarProperties.FESCAR_XID, Collections.singletonList(xid));
        }
        return execution.execute(request, body);
    }

}
