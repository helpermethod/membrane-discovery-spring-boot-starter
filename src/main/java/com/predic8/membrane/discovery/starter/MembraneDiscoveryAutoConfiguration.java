package com.predic8.membrane.discovery.starter;

import com.predic8.membrane.core.interceptor.DispatchingInterceptor;
import com.predic8.membrane.core.interceptor.HTTPClientInterceptor;
import com.predic8.membrane.core.interceptor.RuleMatchingInterceptor;
import com.predic8.membrane.core.interceptor.UserFeatureInterceptor;
import com.predic8.membrane.core.interceptor.rewrite.ReverseProxyingInterceptor;
import com.predic8.membrane.core.transport.Transport;
import com.predic8.membrane.discovery.starter.interceptor.DiscoveryInterceptor;
import com.predic8.membrane.starter.MembraneAutoConfiguration;
import com.predic8.membrane.starter.servlet.ServletTransport;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
@ConditionalOnClass(DiscoveryClient.class)
@AutoConfigureBefore(MembraneAutoConfiguration.class)
public class MembraneDiscoveryAutoConfiguration {
    @Bean
    public DiscoveryInterceptor discoveryInterceptor(DiscoveryClient discoveryClient) {
        return new DiscoveryInterceptor(discoveryClient);
    }

    @Bean
    public Transport transport(DiscoveryInterceptor discoveryInterceptor) {
        ServletTransport transport = new ServletTransport();

        Collections.addAll(
            transport.getInterceptors(),
            new ReverseProxyingInterceptor(),
            new RuleMatchingInterceptor(),
            new DispatchingInterceptor(),
            new UserFeatureInterceptor(),
            discoveryInterceptor,
            new HTTPClientInterceptor());

        return transport;
    }
}