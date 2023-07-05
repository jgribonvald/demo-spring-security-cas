package org.esco.demo.ssc.security;

import org.apereo.cas.client.configuration.ConfigurationKey;
import org.apereo.cas.client.configuration.ConfigurationKeys;
import org.apereo.cas.client.session.SessionMappingStorage;
import org.apereo.cas.client.util.AbstractConfigurationFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by jgribonvald on 01/06/16.
 */

/**
 * Implements the Single Sign Out protocol.  It handles registering the session and destroying the session.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.1
 */
public final class CustomSingleSignOutFilter extends AbstractConfigurationFilter {

    private static final CustomSingleSignOutHandler HANDLER = new CustomSingleSignOutHandler();

    private final AtomicBoolean handlerInitialized = new AtomicBoolean(false);

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);

        if (!isIgnoreInitConfiguration()) {
            HANDLER.setArtifactParameterName(getString(ConfigurationKeys.ARTIFACT_PARAMETER_NAME));
            HANDLER.setLogoutParameterName(getString(ConfigurationKeys.LOGOUT_PARAMETER_NAME));
            HANDLER.setFrontLogoutParameterName(getString(new ConfigurationKey<>("frontLogoutParameterName", "SAMLRequest")));
            HANDLER.setRelayStateParameterName(getString(ConfigurationKeys.RELAY_STATE_PARAMETER_NAME));
            HANDLER.setCasServerUrlPrefix(getString(new ConfigurationKey<>("casServerUrlPrefix", "")));
            HANDLER.setArtifactParameterOverPost(getBoolean(ConfigurationKeys.ARTIFACT_PARAMETER_OVER_POST));
            HANDLER.setEagerlyCreateSessions(getBoolean(ConfigurationKeys.EAGERLY_CREATE_SESSIONS));
        }
        HANDLER.init();
        handlerInitialized.set(true);
    }

    public void setArtifactParameterName(final String name) {
        HANDLER.setArtifactParameterName(name);
    }

    public void setLogoutParameterName(final String name) {
        HANDLER.setLogoutParameterName(name);
    }

    public void setFrontLogoutParameterName(final String name) {
        HANDLER.setFrontLogoutParameterName(name);
    }

    public void setRelayStateParameterName(final String name) {
        HANDLER.setRelayStateParameterName(name);
    }

    public void setCasServerUrlPrefix(final String casServerUrlPrefix) {
        HANDLER.setCasServerUrlPrefix(casServerUrlPrefix);
    }

    public void setSessionMappingStorage(final SessionMappingStorage storage) {
        HANDLER.setSessionMappingStorage(storage);
    }

    public void doFilter(
            final ServletRequest servletRequest,
            final ServletResponse servletResponse,
            final FilterChain filterChain
    ) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        /**
         * <p>Workaround for now for the fact that Spring Security will fail since it doesn't call {@link #init(javax.servlet.FilterConfig)}.</p>
         * <p>Ultimately we need to allow deployers to actually inject their fully-initialized {@link SingleSignOutHandler}.</p>
         */
        if (!this.handlerInitialized.getAndSet(true)) {
            HANDLER.init();
        }

        if (HANDLER.isTokenRequest(request)) {
            filterChain.doFilter(servletRequest, servletResponse);
            HANDLER.process(request, response);
        } else if (HANDLER.process(request, response)) {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {
        // nothing to do
    }

    private static CustomSingleSignOutHandler getSingleSignOutHandler() {
        return HANDLER;
    }

}
