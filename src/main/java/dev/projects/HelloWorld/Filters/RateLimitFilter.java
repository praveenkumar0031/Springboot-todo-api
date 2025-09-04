package dev.projects.HelloWorld.Filters;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Order(2) // Runs after JwtFilter
public class RateLimitFilter extends OncePerRequestFilter {

    // Store buckets per user (or per IP)
    private final ConcurrentHashMap<String, Bucket> cache = new ConcurrentHashMap<>();

    private Bucket createNewBucket() {
        Bandwidth limit = Bandwidth.classic(5, Refill.intervally(5, Duration.ofMinutes(1)));
        return Bucket.builder().addLimit(limit).build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Identify the user (option 1: from JWT, option 2: from IP)
        String userKey = getUserKey(request);

        // Create bucket for this user if not exists
        Bucket bucket = cache.computeIfAbsent(userKey, k -> createNewBucket());

        // Consume a token
        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response); // allowed
        } else {
            response.setStatus(429);
            response.getWriter().write("Too many requests for user: " + userKey);
        }
    }

    // Helper method to extract user key
    private String getUserKey(HttpServletRequest request) {
        // ✅ Option 1: Use JWT username from SecurityContext
        if (request.getUserPrincipal() != null) {

            return request.getUserPrincipal().getName();

        }
        // ✅ Option 2: fallback to client IP
        return request.getRemoteAddr();
    }
}
