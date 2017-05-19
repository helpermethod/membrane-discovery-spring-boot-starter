package com.predic8.membrane.discovery.starter.interceptor;

import com.predic8.membrane.core.exchange.Exchange;
import com.predic8.membrane.core.interceptor.AbstractInterceptor;
import com.predic8.membrane.core.interceptor.Outcome;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;

import java.net.URI;

public class DiscoveryInterceptor extends AbstractInterceptor {
    private final LoadBalancerClient loadBalancerClient;

    public DiscoveryInterceptor(LoadBalancerClient loadBalancerClient) {
        this.loadBalancerClient = loadBalancerClient;
    }

    @Override
    public Outcome handleRequest(Exchange exc) throws Exception {
        exc.getDestinations().replaceAll(this::resolve);

        return Outcome.CONTINUE;
    }

    private String resolve(String destination) {
        URI uri = URI.create(destination);

        if (!uri.getScheme().matches("discovery")) {
            return uri.toString();
        }

        return loadBalancerClient.choose(uri.getHost()).getUri().toString();
    }
}