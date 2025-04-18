package com.lav.dsite.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.lav.dsite.dto.UserOauthRegisterDto;
import com.lav.dsite.dto.UserRedisDto;
import com.lav.dsite.entity.User;
import com.lav.dsite.enums.OauthProvider;
import com.lav.dsite.enums.ResponseStatus;
import com.lav.dsite.exception.DsiteServiceException;
import com.lav.dsite.service.JwtTokenService;
import com.lav.dsite.service.OauthAuthService;
import com.lav.dsite.service.RedisService;
import com.lav.dsite.utils.LogManager;

import io.lettuce.core.RedisException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class Oauth2SuccessHandler implements AuthenticationSuccessHandler {

    @Value("${frontend.redirectUrl.login}")
    private String redirectLoginUrl;
    @Value("${frontend.redirectUrl.link}")
    private String redirectLinkUrl;
    @Value("${frontend.redirectUrl.home}")
    private String redirectHomeUrl;

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;
    @Autowired
    private OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
    @Autowired
    private JwtTokenService jwtTokenService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private OauthAuthService oauthAuthService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken oAuth2Token = (OAuth2AuthenticationToken) authentication;

        String providerId = oAuth2Token.getAuthorizedClientRegistrationId();

        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(providerId);

        String nameAttributeKey = clientRegistration.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        
        OAuth2User oAuth2User = oAuth2Token.getPrincipal();

        String oAuth2Id = oAuth2User.getAttribute(nameAttributeKey);

        // 根據傳來的 state 指定重定向位置
        Map<String, String> stateMap = parseState(request.getParameter("state"));
        String redirectUrl;
        switch (stateMap.get("actionType")) {
            case "login":
                redirectUrl = redirectLoginUrl;
                break;
            case "link":
                redirectUrl = redirectLinkUrl;
                break;
            case "unknown":
            default:
                redirectUrl = redirectHomeUrl;
        }

        // 檢查是否為支援的 OAuth2 供應商，並將供應商名稱轉換為對應的枚舉類別
        OauthProvider oAuthProvider;
        try {
            oAuthProvider = OauthProvider.fromString(providerId);
        } catch (IllegalArgumentException e) {
            LogManager.exception(e);
            clearAndRedirect(response, redirectUrl, ResponseStatus.UNSUPPORTED_OAUTH_PROVIDER);
            return;
        }

        // 獲取 OAuth2 用戶相關資訊
        String email;
        String nickName;
        switch (oAuthProvider) {
            case GOOGLE:
                email = oAuth2User.getAttribute("email");
                nickName = oAuth2User.getAttribute("given_name");
                break;
            default:
                email = oAuth2User.getAttribute("email");
                nickName = oAuth2User.getAttribute("name");
                break;
        }

        // 將 OAuth2 用戶相關資訊封裝成 UserOauthRegisterDto 用於 登入/註冊、綁定帳戶
        UserOauthRegisterDto userOauthRegisterDto = new UserOauthRegisterDto();
        userOauthRegisterDto.setOauthId(oAuth2Id);
        userOauthRegisterDto.setOauthProvider(oAuthProvider);
        userOauthRegisterDto.setOauthEmail(email);
        userOauthRegisterDto.setNickName(nickName);

        // 登入/註冊、綁定帳戶
        User user;
        try {
            switch (stateMap.get("actionType")) {
                case "login":
                    user = oauthAuthService.loginOrRegisterWithOauthAccount(response, userOauthRegisterDto);
                    break;
                case "link":
                    Long userId = Long.parseLong(stateMap.get("userId"));
                    user = oauthAuthService.linkUserWithOauthAccount(userId, userOauthRegisterDto);
                    break;
                case "unknown":
                default:
                    clearAndRedirect(response, redirectUrl, ResponseStatus.INVALID_ARGUMENT);
                    return;
            }
        } catch (DsiteServiceException e) {
            LogManager.exception(e);
            clearAndRedirect(response, redirectUrl, e.getResponseStatus());
            return;
        }

        // 獲取用戶資訊 (AccessToken)
        OAuth2AuthorizedClient authorizedClient = oAuth2AuthorizedClientService.loadAuthorizedClient(providerId, oAuth2Token.getName());
        String accessToken = authorizedClient.getAccessToken().getTokenValue();
        // 將用戶資訊儲存至 Redis 中
        try {
            UserRedisDto userRedisDto = UserRedisDto.fromUser(user, accessToken);
            redisService.saveUser(userRedisDto);

            // TODO: 權限信息 - 寫入，將權限信息寫入redis
            
        } catch (RedisException e) {
            LogManager.exception(e, e);
            clearAndRedirect(response, redirectUrl, ResponseStatus.REDIS_ERROR);
            return;
        }

        // 重定向到前端
        response.sendRedirect(redirectUrl + "?status=SUCCESS");
    }

    // 解析傳來的 state
    private Map<String, String> parseState(String state) {
        Map<String, String> map = new HashMap<>();
        if (state == null) {
            return map;
        }
        String[] split = state.split("&");
        for (String s : split) {
            if (s.startsWith("actionType=")) {
                map.put("actionType", s.split("=")[1]);
            } else if (s.startsWith("userId=")) {
                map.put("userId", s.split("=")[1]);
            }
        }
        return map;
    }

    // 清除身份，並重定向到前端
    private void clearAndRedirect(HttpServletResponse response, String redirectUrl, ResponseStatus responseStatus) throws IOException {
        jwtTokenService.clearJwtFromCookie(response);
        SecurityContextHolder.clearContext();

        System.out.println("oauth2 success, redirectUrl: " + redirectUrl + "?status=FAILURE&error=" + responseStatus.name());

        response.sendRedirect(redirectUrl + "?status=FAILURE&error=" + responseStatus.name());
        return;
    }
}
