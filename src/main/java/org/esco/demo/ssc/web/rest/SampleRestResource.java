package org.esco.demo.ssc.web.rest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author GIP RECIA - Julien Gribonvald
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class SampleRestResource {

	/**
	 * GET /account -> get user
	 */
	@GetMapping(value = "/account", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDetails> getAccount() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		UserDetails springSecurityUser = null;

		if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
			springSecurityUser = (UserDetails) authentication.getPrincipal();
		}
		if (springSecurityUser == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.debug("UserDetails {}", springSecurityUser);

		return new ResponseEntity<>(springSecurityUser, HttpStatus.OK);
	}

}
