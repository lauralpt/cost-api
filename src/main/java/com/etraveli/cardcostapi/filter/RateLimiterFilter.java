package com.etraveli.cardcostapi.filter;

import io.github.bucket4j.Bucket;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import java.io.IOException;

import static java.time.Duration.ofMinutes;


@Component
public class RateLimiterFilter implements Filter {

    Bucket bucket = Bucket.builder()
            .addLimit(limit-> limit.capacity(7000).refillGreedy(600, ofMinutes(1)))
            .build();
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response);
        } else {
            httpResponse.setStatus(HttpServletResponse.SC_REQUEST_TIMEOUT);
            httpResponse.getWriter().write("Request Limit Exceed, wait a few minutes a try again");
        }
    }

}

