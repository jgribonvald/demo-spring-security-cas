package org.esco.demo.ssc.conf;

import lombok.extern.slf4j.Slf4j;
import org.esco.demo.ssc.security.*;
import org.esco.demo.ssc.web.filter.CsrfCookieGeneratorFilter;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfiguration {

    private static final String CAS_URL_LOGIN = "cas.service.login";
    private static final String CAS_URL_LOGOUT = "cas.service.logout";
    private static final String CAS_URL_PREFIX = "cas.url.prefix";
    private static final String CAS_SERVICE_URL = "app.service.security";
    private static final String APP_SERVICE_HOME = "app.service.home";
    private static final String APP_ADMIN_USER_NAME = "app.admin.userName";
    private static final String APP_CORS_ALLOWED_ORIGINS = "app.cors.allowed.origins";

    // preflight cache duration in the browser
    private static final Long MAX_AGE = 600L; // 600 seconds = 10 minutes
    private static final String[] ALLOWED_METHODS = {"GET", "POST", "PUT", "DELETE", "OPTIONS"};
    private static final String[] ALLOWED_HEADERS = {"X-CSRF-TOKEN", "Content-Type", "Accept", "Origin"};

    private final Environment env;
//	@Inject
//	private AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler;
//	@Inject
//	private AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler;
//	@Inject
//	private AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;

    public SecurityConfiguration(Environment env) {
        this.env = env;
    }

    /*
     * START DEMO FUNCTIONS
     */

    @Bean
    public Set<String> adminList() {
        Set<String> admins = new HashSet<>();
        String adminUserName = env.getProperty(APP_ADMIN_USER_NAME);

        admins.add("admin");
        if (adminUserName != null && !adminUserName.isEmpty()) {
            admins.add(adminUserName);
        }

        return admins;
    }

    @Bean
    public AuthenticationUserDetailsService<CasAssertionAuthenticationToken> customUserDetailsService() {
        return new CustomUserDetailsService(adminList());
    }

    @Bean
    public LogoutFilter requestCasGlobalLogoutFilter() {
        LogoutFilter logoutFilter = new LogoutFilter(env.getRequiredProperty(CAS_URL_LOGOUT) + "?service="
                + env.getRequiredProperty(APP_SERVICE_HOME), new SecurityContextLogoutHandler());
//		logoutFilter.setFilterProcessesUrl("/logout");
//		logoutFilter.setFilterProcessesUrl("/j_spring_cas_security_logout");
        logoutFilter.setLogoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"));

        return logoutFilter;
    }

    /*
     * END
     */

    @Bean
    public ServiceProperties serviceProperties() {
        ServiceProperties sp = new ServiceProperties();
        sp.setService(env.getRequiredProperty(CAS_SERVICE_URL));
        sp.setSendRenew(false);
        sp.setAuthenticateAllArtifacts(true);

        return sp;
    }

    @Bean
    public CasAuthenticationProvider casAuthenticationProvider() {
        CasAuthenticationProvider casAuthenticationProvider = new CasAuthenticationProvider();
        casAuthenticationProvider.setAuthenticationUserDetailsService(customUserDetailsService());
        casAuthenticationProvider.setServiceProperties(serviceProperties());
        casAuthenticationProvider.setTicketValidator(cas20ServiceTicketValidator());
        casAuthenticationProvider.setKey("an_id_for_this_auth_provider_only");

        return casAuthenticationProvider;
    }

    @Bean
    public SessionAuthenticationStrategy sessionStrategy() {
        SessionFixationProtectionStrategy sessionStrategy = new SessionFixationProtectionStrategy();
        sessionStrategy.setMigrateSessionAttributes(false);

        return sessionStrategy;
    }

    @Bean
    protected AuthenticationManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(casAuthenticationProvider()));
    }

    @Bean
    public Cas20ServiceTicketValidator cas20ServiceTicketValidator() {
        return new Cas20ServiceTicketValidator(env.getRequiredProperty(CAS_URL_PREFIX));
    }

    @Bean
    public CasAuthenticationFilter casAuthenticationFilter() throws Exception {
        CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
        casAuthenticationFilter.setAuthenticationManager(authenticationManager());
        casAuthenticationFilter.setSessionAuthenticationStrategy(sessionStrategy());
//        casAuthenticationFilter.setAuthenticationFailureHandler(ajaxAuthenticationFailureHandler);
//        casAuthenticationFilter.setAuthenticationSuccessHandler(ajaxAuthenticationSuccessHandler);

        return casAuthenticationFilter;
    }

    @Bean
    public CasAuthenticationEntryPoint casAuthenticationEntryPoint() {
        CasAuthenticationEntryPoint casAuthenticationEntryPoint = new CasAuthenticationEntryPoint();
        casAuthenticationEntryPoint.setLoginUrl(env.getRequiredProperty(CAS_URL_LOGIN));
        casAuthenticationEntryPoint.setServiceProperties(serviceProperties());

        return casAuthenticationEntryPoint;
    }

    @Bean
    public CustomSingleSignOutFilter singleSignOutFilter() {
        CustomSingleSignOutFilter singleSignOutFilter = new CustomSingleSignOutFilter();
        singleSignOutFilter.setCasServerUrlPrefix(env.getRequiredProperty(CAS_URL_PREFIX));

        return singleSignOutFilter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList(env
                .getRequiredProperty(APP_CORS_ALLOWED_ORIGINS, String.class)
                .replaceAll("\\s", "")
                .split(",")
        ));
        corsConfiguration.setAllowedMethods(Arrays.asList(ALLOWED_METHODS));
        corsConfiguration.setAllowedHeaders(Arrays.asList(ALLOWED_HEADERS));
        corsConfiguration.setMaxAge(MAX_AGE);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().antMatchers(
                "/fonts/**",
                "/images/**",
                "/scripts/**",
                "/styles/**",
                "/views/**",
                "/i18n/**"
        );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and()
                .addFilterAfter(new CsrfCookieGeneratorFilter(), CsrfFilter.class).exceptionHandling()
                .authenticationEntryPoint(casAuthenticationEntryPoint()).and()
                .addFilter(casAuthenticationFilter())
                .addFilterBefore(singleSignOutFilter(), CasAuthenticationFilter.class)
                .addFilterBefore(requestCasGlobalLogoutFilter(), LogoutFilter.class);

        http.headers().frameOptions().disable().and()
                .authorizeRequests(authz -> authz
                        .antMatchers("/").permitAll()
                        .antMatchers(
                                "/login",
                                "/logout",
                                "/secure"
                        ).authenticated()
                        .antMatchers("/filtered").hasAuthority(AuthoritiesConstants.ADMIN)
                        .anyRequest().authenticated()
                );

        http
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");

        return http.build();
    }

}
