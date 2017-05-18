package com.predic8.membrane.eureka.starter.interceptor;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.predic8.membrane.core.exchange.Exchange;
import com.predic8.membrane.core.interceptor.AbstractInterceptor;
import com.predic8.membrane.core.interceptor.Outcome;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class EurekaInterceptor extends AbstractInterceptor {
    private final EurekaClient eurekaClient;

    public EurekaInterceptor(EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;
    }

    @Override
    public Outcome handleRequest(Exchange exc) throws Exception {
        exc.getDestinations().replaceAll(this::resolveDestination);

        return Outcome.CONTINUE;
    }

    private String resolveDestination(String destination) {
        URI uri = URI.create(destination);

        try {
            InstanceInfo instanceInfo = eurekaClient.getNextServerFromEureka(uri.getHost(), false);

            return UriComponentsBuilder.newInstance()
                                       .host(instanceInfo.getHostName())
                                       .port(instanceInfo.getPort())
                                       .toUriString();
        } catch (RuntimeException e) {
            return uri.toString();
        }
    }
}