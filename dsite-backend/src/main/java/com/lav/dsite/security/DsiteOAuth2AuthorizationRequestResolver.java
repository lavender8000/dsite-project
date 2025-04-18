package com.lav.dsite.security;

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class DsiteOAuth2AuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

    private final OAuth2AuthorizationRequestResolver defaultResolver;

    public DsiteOAuth2AuthorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository) {
        this.defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(
            clientRegistrationRepository, OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        OAuth2AuthorizationRequest oAuth2AuthorizationRequest = defaultResolver.resolve(request);
        return addStateParameters(oAuth2AuthorizationRequest, request);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        OAuth2AuthorizationRequest oAuth2AuthorizationRequest = defaultResolver.resolve(request, clientRegistrationId);
        return addStateParameters(oAuth2AuthorizationRequest, request);
    }

    private OAuth2AuthorizationRequest addStateParameters(OAuth2AuthorizationRequest oAuth2AuthorizationRequest, HttpServletRequest request) {
        if (oAuth2AuthorizationRequest == null) {
            return null;
        }

        OAuth2AuthorizationRequest.Builder builder = OAuth2AuthorizationRequest.from(oAuth2AuthorizationRequest);

        OAuth2AuthorizationRequest authorizationRequest;
        String originalState = oAuth2AuthorizationRequest.getState();
        if (originalState != null) {
            String newState = originalState;

            String actionType = request.getParameter("actionType");
            if ("login".equalsIgnoreCase(actionType)) {
                newState += "&actionType=login";
            } else if ("link".equalsIgnoreCase(actionType)) {
                newState += "&actionType=link";
            } else {
                newState += "&actionType=unknown";
            }

            String userId = request.getParameter("userId");
            newState = newState + "&userId=" + userId;

            authorizationRequest = builder.state(newState).build();
        } else {
            authorizationRequest = builder.build();
        }

        return authorizationRequest;
    }
}
