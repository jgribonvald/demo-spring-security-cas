package org.esco.demo.ssc.web.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.core.env.Environment;

@Slf4j
public class CrossOriginFilter implements Filter {

	private static final String ENV_APP_CORS = "app.cors.";

	// private RelaxedPropertyResolver propertyResolver;

	// @Override
	// public void setEnvironment(Environment environment) {
	// this.propertyResolver = new RelaxedPropertyResolver(environment,
	// ENV_APP_CORS);
	// }

	// @Override
	// public void doFilter(ServletRequest req, ServletResponse res,
	// FilterChain chain) throws IOException, ServletException {
	// HttpServletResponse response = (HttpServletResponse) res;
	//
	// Set<String> allowedOrigins = new HashSet<String>(
	// Arrays.asList(propertyResolver.getProperty("allowed.origins")
	// .split(",")));
	//
	// String origin = req.getHeader("Origin");
	// if (allowedOrigins.contains(origin)) {
	// response.addHeader("Access-Control-Allow-Origin", origin);
	// return true;
	// }
	// response.setHeader("Access-Control-Allow-Origin", "*");
	// response.setHeader("Access-Control-Allow-Methods",
	// "POST, GET, PUT, DELETE");
	// response.setHeader("Access-Control-Max-Age", "3600");
	// response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
	// chain.doFilter(req, res);
	// }
	//
	// @Override
	// public void init(FilterConfig filterConfig) {
	// }
	//
	// @Override
	// public void destroy() {
	// }

	private List<String> allowedOrigins = new ArrayList<String>();

	public CrossOriginFilter(Environment environment) throws ServletException {
		String origins = new RelaxedPropertyResolver(environment, ENV_APP_CORS).getRequiredProperty("allowed.origins",
				String.class);
		this.setAllowedOrigins(origins);
	}

	// preflight cache duration in the browser
	private String maxAge = "600"; // 600 seconds = 10 minutes
	private String allowedMethods = "GET,POST,PUT,DELETE";
	private String allowedHeaders = "X-CSRF-TOKEN,Content-Type,Accept,Origin";

	public void destroy() {
	}

	public void init(FilterConfig config) {
	}

	public void setAllowedOrigins(String origins) {
		allowedOrigins.clear();
		allowedOrigins.addAll(Arrays.asList(origins.split(",")));
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		handle((HttpServletRequest) request, (HttpServletResponse) response, chain);
	}

	private void handle(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String origin = request.getHeader("Origin");
		log.debug("origin {}", origin);
		log.debug("allowed origins {}", allowedOrigins);
		if (origin != null && allowedOrigins.contains(origin)) {
			response.setHeader("Access-Control-Allow-Origin", origin);
			response.setHeader("Access-Control-Allow-Credentials", "true");
			if (isPreflightRequest(request)) {
				response.setHeader("Access-Control-Max-Age", maxAge);
				response.setHeader("Access-Control-Allow-Methods", allowedMethods);
				response.setHeader("Access-Control-Allow-Headers", allowedHeaders);
				return;
			}
		}
		chain.doFilter(request, response);
	}

	private boolean isPreflightRequest(HttpServletRequest request) {
		return "OPTIONS".equalsIgnoreCase(request.getMethod())
				&& request.getHeader("Access-Control-Request-Method") != null;
	}

}
