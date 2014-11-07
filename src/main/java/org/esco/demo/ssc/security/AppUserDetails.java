package org.esco.demo.ssc.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AppUserDetails implements UserDetails {

	/** */
	private static final long serialVersionUID = -4777124807325532850L;

	private UserDetails user;

	public AppUserDetails() {
		super();
	}

	/**
	 * @param user
	 * @param userFactory
	 */
	public AppUserDetails(UserDetails user) {
		super();
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> l = new ArrayList<GrantedAuthority>();
		l.add(new GrantedAuthority() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getAuthority() {
				return "ROLE_AUTHENTICATED";
			}
		});
		return l;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
