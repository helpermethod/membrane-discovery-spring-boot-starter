package com.predic8.membrane.eureka.starter;

import com.netflix.discovery.EurekaClient;
import com.predic8.membrane.ServletTransport;
import com.predic8.membrane.core.interceptor.DispatchingInterceptor;
import com.predic8.membrane.core.interceptor.HTTPClientInterceptor;
import com.predic8.membrane.core.interceptor.RuleMatchingInterceptor;
import com.predic8.membrane.core.interceptor.UserFeatureInterceptor;
import com.predic8.membrane.core.interceptor.rewrite.ReverseProxyingInterceptor;
import com.predic8.membrane.core.transport.Transport;
import com.predic8.membrane.eureka.starter.interceptor.EurekaInterceptor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
@ConditionalOnClass(EurekaClient.class)
@AutoConfigureBefore(name = "com.predic8.membrane.starter.MembraneAutoConfiguration")
public class MembraneEurekaAutoConfiguration {
    @Bean
    public EurekaInterceptor eurekaInterceptor(EurekaClient eurekaClient) {
        return new EurekaInterceptor(eurekaClient);
    }

    @Bean
    public Transport transport(EurekaInterceptor eurekaInterceptor) {
        ServletTransport transport = new ServletTransport();

        Collections.addAll(
            transport.getInterceptors(),
            new ReverseProxyingInterceptor(),
            new RuleMatchingInterceptor(),
            new DispatchingInterceptor(),
            new UserFeatureInterceptor(),
            eurekaInterceptor,
            new HTTPClientInterceptor());

        return transport;
    }
}