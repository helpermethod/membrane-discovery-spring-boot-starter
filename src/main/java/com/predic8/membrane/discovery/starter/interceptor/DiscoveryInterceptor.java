package com.predic8.membrane.discovery.starter.interceptor;

import com.predic8.membrane.core.exchange.Exchange;
import com.predic8.membrane.core.interceptor.AbstractInterceptor;
import com.predic8.membrane.core.interceptor.Outcome;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.net.URI;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DiscoveryInterceptor extends AbstractInterceptor {
    private final AtomicInteger index = new AtomicInteger();
    private final DiscoveryClient discoveryClient;

    public DiscoveryInterceptor(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Override
    public Outcome handleRequest(Exchange exc) throws Exception {
        exc.getDestinations().replaceAll(this::resolveDestination);

        return Outcome.CONTINUE;
    }

    private String resolveDestination(String destination) {
        URI uri = URI.create(destination);

        if (!uri.getScheme().matches("discovery")) {
            return uri.toString();
        }

        List<ServiceInstance> instances = discoveryClient.getInstances(uri.getHost());
        ServiceInstance instance = instances.get(index.getAndIncrement() % instances.size());

        return instance.getUri().toString();
    }
}