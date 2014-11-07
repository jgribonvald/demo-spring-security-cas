package org.esco.demo.ssc.conf;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.esco.demo.ssc.security.AjaxAuthenticationFailureHandler;
import org.esco.demo.ssc.security.AjaxAuthenticationSuccessHandler;
import org.esco.demo.ssc.security.AjaxLogoutSuccessHandler;
import org.esco.demo.ssc.security.CustomUserDetailsService;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Inject
	private Environment env;

	@Inject
	private AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler;

	@Inject
	private AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler;

	@Inject
	private AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;

	@Inject
	private CasAuthenticationProvider authProvider;

	@Bean
	public ServiceProperties serviceProperties() {
		ServiceProperties sp = new ServiceProperties();
		sp.setSendRenew(false);
		sp.setService(env.getProperty("app.service.security"));
		return sp;
	}

	@Bean
	public Set<String> adminList() {
		Set<String> admins = new HashSet<String>();
		String adminUserName = env.getProperty("app.admin.userName");

		admins.add("admin");
		if (adminUserName != null && !adminUserName.isEmpty()) {
			admins.add(adminUserName);
		}
		return admins;
	}

	@SuppressWarnings("rawtypes")
	@Inject
	private AuthenticationUserDetailsService customUserDetailsService() {
		return new CustomUserDetailsService(adminList());
	}

	@Bean
	public CasAuthenticationProvider casAuthenticationProvider() {
		CasAuthenticationProvider casAuthenticationProvider = new CasAuthenticationProvider();
		casAuthenticationProvider
				.setAuthenticationUserDetailsService(customUserDetailsService());
		casAuthenticationProvider.setServiceProperties(serviceProperties());
		casAuthenticationProvider
				.setTicketValidator(cas20ServiceTicketValidator());
		casAuthenticationProvider.setKey("an_id_for_this_auth_provider_only");
		return casAuthenticationProvider;
	}

	@Bean
	public Cas20ServiceTicketValidator cas20ServiceTicketValidator() {
		return new Cas20ServiceTicketValidator(
				env.getProperty("cas.service.url"));
	}

	@Bean
	public CasAuthenticationFilter casAuthenticationFilter() throws Exception {
		CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
		casAuthenticationFilter
				.setAuthenticationManager(authenticationManager());
		return casAuthenticationFilter;
	}

	@Bean
	public CasAuthenticationEntryPoint casAuthenticationEntryPoint() {
		CasAuthenticationEntryPoint ep = new CasAuthenticationEntryPoint();
		ep.setLoginUrl(env.getProperty("cas.service.loginUrl"));
		ep.setServiceProperties(serviceProperties());
		return ep;
	}

	@Inject
	public void configureGlobal(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.authenticationProvider(authProvider);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/bower_components/**")
				.antMatchers("/fonts/**").antMatchers("/images/**")
				.antMatchers("/scripts/**").antMatchers("/styles/**")
				.antMatchers("/views/**").antMatchers("/i18n/**")
				.antMatchers("/swagger-ui/**").antMatchers("/console/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.exceptionHandling()
				.authenticationEntryPoint(casAuthenticationEntryPoint()).and()
				.addFilter(casAuthenticationFilter()).logout()
				.logoutUrl("/app/logout")
				.logoutSuccessHandler(ajaxLogoutSuccessHandler)
				.deleteCookies("JSESSIONID").permitAll().and().csrf().disable()
				.headers().frameOptions().disable().authorizeRequests()
				.antMatchers("/app/rest/register").permitAll()
				.antMatchers("/app/rest/activate").permitAll()
				.antMatchers("/app/rest/authenticate").permitAll()
				.antMatchers("/cas").authenticated().antMatchers("/app/**")
				.authenticated().antMatchers("/protected/**").authenticated();
	}

	@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
	private static class GlobalSecurityConfiguration extends
			GlobalMethodSecurityConfiguration {
	}
}
