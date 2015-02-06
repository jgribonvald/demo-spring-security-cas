package org.esco.demo.ssc.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AppUserDetails implements UserDetails {

	/** */
	private static final long serialVersionUID = -4777124807325532850L;

	private String userid;

	private Collection<? extends GrantedAuthority> authorities;

	private List<String> roles;

	public AppUserDetails() {
		super();
	}

	/**
	 * @param user
	 * @param userFactory
	 */
	public AppUserDetails(String userid, Collection<? extends GrantedAuthority> authorities) {
		super();
		this.userid = userid;
		this.authorities = authorities;
		this.roles = new ArrayList<>();
		for (GrantedAuthority authority : authorities) {
			this.roles.add(authority.getAuthority());
		}
	}

	public String getUserid() {
		return userid;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		/*
		 * List<GrantedAuthority> l = new ArrayList<GrantedAuthority>(); l.add(new
		 * GrantedAuthority() { private static final long serialVersionUID = 1L;
		 * 
		 * @Override public String getAuthority() { return "ROLE_AUTHENTICATED"; } }); return l;
		 */
		return authorities;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return userid;
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

	@Override
	public String toString() {
		return "UserDetails [userid=" + userid + ", authorities=" + roles.toString() + ", isAccountNonExpired()="
				+ isAccountNonExpired() + ", isAccountNonLocked()=" + isAccountNonLocked()
				+ ", isCredentialsNonExpired()=" + isCredentialsNonExpired() + ", isEnabled()=" + isEnabled() + "]";
	}

}
