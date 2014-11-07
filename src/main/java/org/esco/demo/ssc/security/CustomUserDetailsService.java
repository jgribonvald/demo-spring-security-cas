package org.esco.demo.ssc.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Authenticate a user from the database.
 */
// @Component("userDetailsService")
public class CustomUserDetailsService implements
		AuthenticationUserDetailsService<Authentication> {

	private final Logger log = LoggerFactory
			.getLogger(CustomUserDetailsService.class);

	private Set<String> admins;

	/**
	 * @param admins
	 */
	public CustomUserDetailsService(Set<String> admins) {
		super();
		this.admins = admins;
	}

	@Override
	public UserDetails loadUserDetails(Authentication token)
			throws UsernameNotFoundException {
		String login = token.getPrincipal().toString();
		String lowercaseLogin = login.toLowerCase();

		log.debug("Authenticating {}", login);
		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();

		if (admins != null && admins.contains(lowercaseLogin)) {
			grantedAuthorities.add(new SimpleGrantedAuthority(
					AuthoritiesConstants.ADMIN));
		}
		grantedAuthorities.add(new GrantedAuthority() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getAuthority() {
				return "ROLE_AUTHENTICATED";
			}
		});

		return new org.springframework.security.core.userdetails.User(
				lowercaseLogin, lowercaseLogin, grantedAuthorities);
	}
}
