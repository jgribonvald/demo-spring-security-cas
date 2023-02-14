package org.esco.demo.ssc.web.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class AccountResource {

//	 @Inject
//	 private ServletContext servletContext;

//	 @Inject
//	 private ApplicationContext applicationContext;

	/**
	 * GET /authenticate -> check if the user is authenticated, and return its login.
	 */
	@GetMapping(value = "authenticate", produces = MediaType.APPLICATION_JSON_VALUE)
	public String isAuthenticated(HttpServletRequest request) {
		log.debug("REST request to check if the current user is authenticated");
		return request.getRemoteUser();
	}

}
