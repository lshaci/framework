package top.lshaci.framework.fescar.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.alibaba.fescar.spring.annotation.GlobalTransactionScanner;

import top.lshaci.framework.fescar.filter.FescarRMRequestFilter;
import top.lshaci.framework.fescar.interceptor.FescarRestInterceptor;

@Configuration
public class FescarAutoConfiguration {

    public static final String FESCAR_XID = "fescarXID";

    @Bean
    public GlobalTransactionScanner globalTransactionScanner(Environment environment){
    	System.out.println("FescarAutoConfiguration.globalTransactionScanner()");
        String applicationName = environment.getProperty("spring.application.name");
        String groupName = environment.getProperty("fescar.group.name");
        if(applicationName == null){
            return new GlobalTransactionScanner(groupName == null ? "my_test_tx_group" : groupName);
        }else{
            return new GlobalTransactionScanner(applicationName, groupName == null ? "my_test_tx_group" : groupName);
        }
    }

//    @Bean
//    public Object addFescarInterceptor(Collection<RestTemplate> restTemplates){
//    	System.out.println("FescarAutoConfiguration.addFescarInterceptor()");
//        restTemplates.stream()
//                .forEach(restTemplate -> {
//                    List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
//                    if(interceptors != null){
//                        interceptors.add(fescarRestInterceptor());
//                    }
//                });
//        return new Object();
//    }

    @Bean
    public FescarRMRequestFilter fescarRMRequestFilter(){
    	System.out.println("FescarAutoConfiguration.fescarRMRequestFilter()");
        return new FescarRMRequestFilter();
    }

    @Bean
    public FescarRestInterceptor fescarRestInterceptor(){
    	System.out.println("FescarAutoConfiguration.fescarRestInterceptor()");
        return new FescarRestInterceptor();
    }

}
