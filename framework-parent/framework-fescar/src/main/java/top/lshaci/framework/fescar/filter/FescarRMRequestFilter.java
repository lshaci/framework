package top.lshaci.framework.fescar.filter;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.alibaba.fescar.core.context.RootContext;

import lombok.extern.slf4j.Slf4j;
import top.lshaci.framework.fescar.properties.FescarProperties;

@Slf4j
public class FescarRMRequestFilter extends OncePerRequestFilter {
	
	@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String xid = RootContext.getXID();
        String requestXid = request.getHeader(FescarProperties.FESCAR_XID);
        
        boolean bind = false;
        if(StringUtils.isBlank(xid) && StringUtils.isNotBlank(requestXid)){
            RootContext.bind(requestXid);
            bind = true;
            log.info("Current request bind XID: {}", requestXid);
        }
        try{
            filterChain.doFilter(request, response);
        } finally {
            if (bind) {
                String unbindXid = RootContext.unbind();
                if (!Objects.equals(unbindXid, requestXid)) {
                	log.info("XID is changed when request execute, check if it meets expectations please");
				}
            }
        }
    }
}
