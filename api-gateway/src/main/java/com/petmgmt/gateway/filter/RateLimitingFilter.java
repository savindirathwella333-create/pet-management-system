package com.petmgmt.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Requirement 3: Rate Limiting - throttles requests per client IP.
 *
 * Implemented as a simple in-memory fixed-window counter (max N requests per
 * window per IP), reset on a schedule. This intentionally avoids requiring a
 * Redis dependency (Spring Cloud Gateway's built-in RequestRateLimiter needs
 * Redis) to keep the project's infrastructure footprint small, per the "not a
 * full complete system" scope of this coursework. For production this would
 * be swapped for a Redis-backed distributed limiter.
 */
@Component
public class RateLimitingFilter implements GlobalFilter, Ordered {

    private static final int MAX_REQUESTS_PER_WINDOW = 30;
    private static final long WINDOW_MILLIS = 60_000; // 1 minute

    private final ConcurrentHashMap<String, Window> windows = new ConcurrentHashMap<>();

    private static class Window {
        final AtomicInteger count = new AtomicInteger(0);
        volatile long windowStart = System.currentTimeMillis();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String clientIp = resolveClientIp(exchange.getRequest());
        Window window = windows.computeIfAbsent(clientIp, k -> new Window());

        long now = System.currentTimeMillis();
        synchronized (window) {
            if (now - window.windowStart > WINDOW_MILLIS) {
                window.windowStart = now;
                window.count.set(0);
            }
            if (window.count.incrementAndGet() > MAX_REQUESTS_PER_WINDOW) {
                return tooManyRequests(exchange);
            }
        }

        return chain.filter(exchange);
    }

    private String resolveClientIp(ServerHttpRequest request) {
        InetSocketAddress remote = request.getRemoteAddress();
        return remote != null && remote.getAddress() != null
                ? remote.getAddress().getHostAddress()
                : "unknown";
    }

    private Mono<Void> tooManyRequests(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
        response.getHeaders().add("Content-Type", "application/json");
        String body = "{\"error\":\"Too Many Requests\",\"message\":\"Rate limit exceeded, try again shortly\"}";
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return -2; // run before the JWT filter, so abusive clients are cut off earliest
    }
}
