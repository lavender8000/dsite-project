package com.lav.dsite.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lav.dsite.entity.OauthAccount;
import com.lav.dsite.enums.OauthProvider;

public interface OauthAccountRepository extends JpaRepository<OauthAccount, Long> {

    OauthAccount findByOauthEmail(String oauthEmail);

    List<OauthAccount> findByUserId(Long userId);

    @EntityGraph(attributePaths = { "user" })
    OauthAccount findByOauthIdAndOauthProvider(String oauthId, OauthProvider oauthProvider);
}
