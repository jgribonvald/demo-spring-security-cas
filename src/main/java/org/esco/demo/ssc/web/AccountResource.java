package org.esco.demo.ssc.web;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.spring4.SpringTemplateEngine;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/app")
public class AccountResource {

	private final Logger log = LoggerFactory.getLogger(AccountResource.class);

	@Inject
	private ServletContext servletContext;

	@Inject
	private ApplicationContext applicationContext;

	@Inject
	private SpringTemplateEngine templateEngine;

	/**
	 * GET /rest/authenticate -> check if the user is authenticated, and return
	 * its login.
	 */
	@RequestMapping(value = "/rest/authenticate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String isAuthenticated(HttpServletRequest request) {
		log.debug("REST request to check if the current user is authenticated");
		return request.getRemoteUser();
	}

}
