package org.esco.demo.ssc.conf;

import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.server.MimeMappings;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;

import javax.servlet.ServletContext;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Configuration of web application with Servlet 3.0 APIs.
 */
@Configuration
@Slf4j
public class WebConfigurer implements ServletContextInitializer, WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

    @Inject
    private Environment env;

    @Override
    public void onStartup(ServletContext servletContext) {
        try {
            log.info("Web application configuration, using profiles: {}", Arrays.toString(env.getActiveProfiles()));
        } catch (Exception ignored) {
            log.warn("No Spring profile configured");
        }
//        EnumSet<DispatcherType> disps = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC);
//        if (env.acceptsProfiles(Profiles.of(Constants.SPRING_PROFILE_PRODUCTION))) {
//            initCachingHttpHeadersFilter(servletContext, disps);
//            initStaticResourcesProductionFilter(servletContext, disps);
//            initGzipFilter(servletContext, disps);
//        }
//        if (env.acceptsProfiles(Constants.SPRING_PROFILE_DEVELOPMENT)) {
//            initH2Console(servletContext);
//        }
        log.info("Web application fully configured");
    }

    /**
     * Set up Mime types.
     */
    @Override
    public void customize(ConfigurableServletWebServerFactory container) {
        MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
        // IE issue, see https://github.com/jhipster/generator-jhipster/pull/711
        mappings.add("html", MediaType.TEXT_HTML_VALUE + ";charset=" + StandardCharsets.UTF_8.name().toLowerCase());
        // CloudFoundry issue, see https://github.com/cloudfoundry/gorouter/issues/64
        mappings.add("json", MediaType.TEXT_HTML_VALUE + ";charset=" + StandardCharsets.UTF_8.name().toLowerCase());
        container.setMimeMappings(mappings);
    }

//    /**
//     * Initializes the GZip filter.
//     */
//     private void initGzipFilter(ServletContext servletContext, EnumSet<DispatcherType> disps) {
//         log.debug("Registering GZip Filter");
//         FilterRegistration.Dynamic compressingFilter = servletContext.addFilter("gzipFilter", new GZipServletFilter());
//         Map<String, String> parameters = new HashMap<>();
//
//         compressingFilter.setInitParameters(parameters);
//         compressingFilter.addMappingForUrlPatterns(disps, true, "*.css");
//         compressingFilter.addMappingForUrlPatterns(disps, true, "*.json");
//         compressingFilter.addMappingForUrlPatterns(disps, true, "*.html");
//         compressingFilter.addMappingForUrlPatterns(disps, true, "*.js");
//         compressingFilter.addMappingForUrlPatterns(disps, true, "/api/*");
//         compressingFilter.addMappingForUrlPatterns(disps, true, "/management/*");
//         compressingFilter.setAsyncSupported(true);
//     }

//    /**
//     * Initializes the static resources production Filter.
//     */
//     private void initStaticResourcesProductionFilter(ServletContext servletContext, EnumSet<DispatcherType> disps) {
//         log.debug("Registering static resources Filter");
//         FilterRegistration.Dynamic staticResourcesProductionFilter = servletContext
//                 .addFilter("staticResourcesProductionFilter", new StaticResourcesProductionFilter());
//
//         staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true, "/");
//         staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true, "/index.jsp");
//         staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true, "/images/*");
//         staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true, "/fonts/*");
//         staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true, "/scripts/*");
//         staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true, "/styles/*");
//         staticResourcesProductionFilter.addMappingForUrlPatterns(disps, true, "/views/*");
//         staticResourcesProductionFilter.setAsyncSupported(true);
//     }

//    /**
//     * Initializes the cachig HTTP Headers Filter.
//     */
//     private void initCachingHttpHeadersFilter(ServletContext servletContext,  EnumSet<DispatcherType> disps) {
//         log.debug("Registering Caching HTTP Headers Filter");
//         FilterRegistration.Dynamic cachingHttpHeadersFilter = servletContext
//                 .addFilter("cachingHttpHeadersFilter", new CachingHttpHeadersFilter());
//
//         cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/images/*");
//         cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/fonts/*");
//         cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/scripts/*");
//         cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/styles/*");
//         cachingHttpHeadersFilter.setAsyncSupported(true);
//     }

//    /**
//     * Initializes H2 console
//     */
//    private void initH2Console(ServletContext servletContext) {
//		if (env.getProperty("spring.h2.console.enabled", Boolean.class, false)) {
//			log.debug("Initialize H2 console");
//			H2ConfigurationHelper.initH2Console(servletContext);
//		}
//	}

}
