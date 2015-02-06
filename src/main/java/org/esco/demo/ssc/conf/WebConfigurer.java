package org.esco.demo.ssc.conf;

import java.util.Arrays;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.MimeMappings;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Configuration of web application with Servlet 3.0 APIs.
 */
@Configuration
public class WebConfigurer implements ServletContextInitializer, EmbeddedServletContainerCustomizer {

	private final Logger log = LoggerFactory.getLogger(WebConfigurer.class);

	@Inject
	private Environment env;

	//
	// @Inject
	// private MetricRegistry metricRegistry;

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		log.info("Web application configuration, using profiles: {}", Arrays.toString(env.getActiveProfiles()));
		// EnumSet<DispatcherType> disps = EnumSet
		// .of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC);
		// initMetrics(servletContext, disps);
		// if (env.acceptsProfiles(Constants.SPRING_PROFILE_PRODUCTION)) {
		// initCachingHttpHeadersFilter(servletContext, disps);
		// initStaticResourcesProductionFilter(servletContext, disps);
		// }
		// initGzipFilter(servletContext, disps);
		// if (env.acceptsProfiles(Constants.SPRING_PROFILE_DEVELOPMENT)) {
		// initH2Console(servletContext);
		// }

		log.info("Web application fully configured");
	}

	/**
	 * Set up Mime types.
	 */
	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
		// IE issue, see https://github.com/jhipster/generator-jhipster/pull/711
		mappings.add("html", "text/html;charset=utf-8");
		// CloudFoundry issue, see
		// https://github.com/cloudfoundry/gorouter/issues/64
		mappings.add("json", "text/html;charset=utf-8");
		container.setMimeMappings(mappings);
	}
	/**
	 * Initializes the GZip filter.
	 */
	// private void initGzipFilter(ServletContext servletContext,
	// EnumSet<DispatcherType> disps) {
	// log.debug("Registering GZip Filter");
	// FilterRegistration.Dynamic compressingFilter = servletContext
	// .addFilter("gzipFilter", new GZipServletFilter());
	// Map<String, String> parameters = new HashMap<>();
	// compressingFilter.setInitParameters(parameters);
	// compressingFilter.addMappingForUrlPatterns(disps, true, "*.css");
	// compressingFilter.addMappingForUrlPatterns(disps, true, "*.json");
	// compressingFilter.addMappingForUrlPatterns(disps, true, "*.html");
	// compressingFilter.addMappingForUrlPatterns(disps, true, "*.js");
	// compressingFilter.addMappingForUrlPatterns(disps, true, "/app/rest/*");
	// compressingFilter.addMappingForUrlPatterns(disps, true, "/metrics/*");
	// compressingFilter.setAsyncSupported(true);
	// }

	/**
	 * Initializes the static resources production Filter.
	 */
	// private void initStaticResourcesProductionFilter(ServletContext servletContext,
	// EnumSet<DispatcherType> disps) {
	//
	// log.debug("Registering static resources Filter");
	// FilterRegistration.Dynamic staticResourcesProductionFilter = servletContext.addFilter(
	// "staticResourcesProductionFilter", new StaticResourcesProductionFilter());
	//
	// staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true, "/");
	// staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true, "/index.jsp");
	// staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true, "/images/*");
	// staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true, "/fonts/*");
	// staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true, "/scripts/*");
	// staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true, "/styles/*");
	// staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true, "/views/*");
	// staticResourcesProductionFilter.setAsyncSupported(true);
	// }

	/**
	 * Initializes the cachig HTTP Headers Filter.
	 */
	// private void initCachingHttpHeadersFilter(ServletContext servletContext,
	// EnumSet<DispatcherType> disps) {
	// log.debug("Registering Caching HTTP Headers Filter");
	// FilterRegistration.Dynamic cachingHttpHeadersFilter = servletContext
	// .addFilter("cachingHttpHeadersFilter",
	// new CachingHttpHeadersFilter());
	//
	// cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true,
	// "/images/*");
	// cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true,
	// "/fonts/*");
	// cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true,
	// "/scripts/*");
	// cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true,
	// "/styles/*");
	// cachingHttpHeadersFilter.setAsyncSupported(true);
	// }
}
