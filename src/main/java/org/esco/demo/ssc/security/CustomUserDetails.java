package org.esco.demo.ssc.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private static final long serialVersionUID = -4777124807325532850L;

    @Getter
    private String userid;

    @Getter
    @JsonIgnore
    private Collection<? extends GrantedAuthority> authorities;

    @Getter
    private List<String> roles;

    public CustomUserDetails() {
        super();
    }

    /**
     * @param userid
     * @param authorities
     */
    public CustomUserDetails(String userid, Collection<? extends GrantedAuthority> authorities) {
        super();
        this.userid = userid;
        this.authorities = authorities;
        this.roles = new ArrayList<>();
        for (GrantedAuthority authority : authorities) {
            this.roles.add(authority.getAuthority());
        }
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
        return "UserDetails [userid=" + userid
                + ", authorities=" + roles.toString()
                + ", isAccountNonExpired()=" + isAccountNonExpired()
                + ", isAccountNonLocked()=" + isAccountNonLocked()
                + ", isCredentialsNonExpired()=" + isCredentialsNonExpired()
                + ", isEnabled()=" + isEnabled() + "]";
    }

}
