package com.lav.dsite.service;

import java.util.List;

import com.lav.dsite.entity.OauthAccount;
import com.lav.dsite.enums.OauthProvider;

public interface OauthAccountService {

    OauthAccount saveOauthAccount(OauthAccount oauthAccount);

    OauthAccount findOauthAccountByOauthEmail(String oauthEmail);

    OauthAccount findOauthAccountByOauthIdAndOauthProvider(String oauthId, OauthProvider oauthProvider);

    List<OauthAccount> findOauthAccountsByUserId(Long userId);

}
