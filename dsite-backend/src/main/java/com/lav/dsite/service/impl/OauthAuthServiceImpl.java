package com.lav.dsite.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lav.dsite.dto.UserOauthRegisterDto;
import com.lav.dsite.entity.OauthAccount;
import com.lav.dsite.entity.User;
import com.lav.dsite.enums.DsiteAuthType;
import com.lav.dsite.enums.OauthProvider;
import com.lav.dsite.enums.ResponseStatus;
import com.lav.dsite.exception.DsiteAuthenticationException;
import com.lav.dsite.service.JwtTokenService;
import com.lav.dsite.service.OauthAccountService;
import com.lav.dsite.service.OauthAuthService;
import com.lav.dsite.service.UserService;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class OauthAuthServiceImpl implements OauthAuthService {

    @Autowired
    private OauthAccountService oauthAccountService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenService jwtTokenService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public User linkUserWithOauthAccount(Long userId, UserOauthRegisterDto userOauthRegisterDto) {

        User user = userService.findUserById(userId);

        if (user == null) {
            throw new DsiteAuthenticationException(ResponseStatus.USER_NOT_FOUND);
        }

        // 確認該 OauthAccount 資訊是否已經註冊
        OauthAccount oauthAccount = oauthAccountService.findOauthAccountByOauthIdAndOauthProvider(userOauthRegisterDto.getOauthId(), userOauthRegisterDto.getOauthProvider());
        if (oauthAccount != null) {
            throw new DsiteAuthenticationException(ResponseStatus.USER_OAUTH_ACCOUNT_EXISTS);   
        }

        // 若該 OauthAccount 資訊的信箱與當前用戶的信箱不相同，則需要檢查該信箱是否已經有註冊於 User 表中
        // 避免一個信箱存在於兩個帳號
        // 例如: A@email 的 User 關聯(綁定) B@email 的 OauthAccount，但是又存在一個 B@email 的 User
        if (!(user.getEmail().equals(userOauthRegisterDto.getOauthEmail()))) {
            User userByOauthEmail = userService.findUserByEmail(userOauthRegisterDto.getOauthEmail());
            if (userByOauthEmail != null) {
                throw new DsiteAuthenticationException(ResponseStatus.USER_OAUTH_ACCOUNT_EXISTS);   
            }
        }
        
        // 將新的 OauthAccount 資訊與當前用戶關聯(綁定)
        OauthAccount newOauthAccount = new OauthAccount();
        newOauthAccount.setOauthId(userOauthRegisterDto.getOauthId());
        newOauthAccount.setOauthProvider(userOauthRegisterDto.getOauthProvider());
        newOauthAccount.setOauthEmail(userOauthRegisterDto.getOauthEmail());
        newOauthAccount.setUser(user);
        oauthAccount = oauthAccountService.saveOauthAccount(newOauthAccount);
        
        return user;
    }
    
    @Transactional(rollbackFor = Exception.class)
    @Override
    public User loginOrRegisterWithOauthAccount(HttpServletResponse response, UserOauthRegisterDto userOauthRegisterDto) {

        String oAuth2Id = userOauthRegisterDto.getOauthId();
        OauthProvider oAuthProvider = userOauthRegisterDto.getOauthProvider();

        // 查詢是否已經註冊過相同的 oauthId 並對應的 oauthProvider
        OauthAccount oauthAccount = oauthAccountService.findOauthAccountByOauthIdAndOauthProvider(oAuth2Id, oAuthProvider);
        User user;
        if (oauthAccount != null) {
            user = oauthAccount.getUser();
        } else {
            // 註冊新用戶
            user = userService.registerUserWithOauth(userOauthRegisterDto);
        }

        // 生成 JWT token 並寫入 cookie
        String jwtToken = jwtTokenService.generateAccessToken(user, DsiteAuthType.OAUTH);
        jwtTokenService.addJwtToCookie(jwtToken, response);

        return user;
    }

}
