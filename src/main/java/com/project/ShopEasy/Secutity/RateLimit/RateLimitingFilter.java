package com.project.ShopEasy.Secutity.RateLimit;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RateLimitingFilter implements Filter {

	private final Map<String, Bucket> bucketCache = new ConcurrentHashMap<>();

	private Bucket createNewBucket() {
		return Bucket.builder().addLimit(Bandwidth.classic(3, Refill.greedy(3, Duration.ofMinutes(5)))).build();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub

		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;

		if (httpServletRequest.getAttribute("FILTER_APPLIED") != null) {
			chain.doFilter(request, response);
			return;
		}
		httpServletRequest.setAttribute("FILTER_APPLIED", true);

		if (httpServletRequest.getRequestURI().startsWith("/api/v1/users/send-otp")) {
			String ipAddress = request.getRemoteAddr();
			Bucket bucket = bucketCache.computeIfAbsent(ipAddress, k -> createNewBucket());

			System.out
					.println("IP: " + ipAddress + " - Available Tokens Before Request: " + bucket.getAvailableTokens());

			ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

			System.out
					.println("IP: " + ipAddress + " - Available Tokens After Request: " + bucket.getAvailableTokens());

			if (!probe.isConsumed()) {
				httpServletResponse.setStatus(429);
				httpServletResponse.getWriter().write("Too many OTP requests!! Please try again later");
				return;
			}
		}

		System.out.println("🔍 Before executing chain.doFilter()");
		chain.doFilter(httpServletRequest, httpServletResponse);
		System.out.println("✅ After executing chain.doFilter()");

	}

}
