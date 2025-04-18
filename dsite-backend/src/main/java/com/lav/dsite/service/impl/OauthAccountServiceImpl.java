package com.lav.dsite.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lav.dsite.entity.OauthAccount;
import com.lav.dsite.enums.OauthProvider;
import com.lav.dsite.enums.ResponseStatus;
import com.lav.dsite.exception.OauthAccountServiceException;
import com.lav.dsite.repository.OauthAccountRepository;
import com.lav.dsite.service.OauthAccountService;

@Service
public class OauthAccountServiceImpl implements OauthAccountService {

    @Autowired
    private OauthAccountRepository oauthAccountRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OauthAccount saveOauthAccount(OauthAccount oauthAccount) {
        try {
            return oauthAccountRepository.save(oauthAccount);
        } catch (DataIntegrityViolationException e) {
            throw new OauthAccountServiceException(ResponseStatus.USER_OAUTH_ACCOUNT_EXISTS, e);
        } catch (OptimisticLockingFailureException e) {
            throw new OauthAccountServiceException(ResponseStatus.OPTIMISTIC_LOCK_EXCEPTION, e);
        }
    }

    @Override
    public OauthAccount findOauthAccountByOauthIdAndOauthProvider(String oauthId, OauthProvider oauthProvider) {
        return oauthAccountRepository.findByOauthIdAndOauthProvider(oauthId, oauthProvider);
    }

    @Override
    public OauthAccount findOauthAccountByOauthEmail(String oauthEmail) {
        return oauthAccountRepository.findByOauthEmail(oauthEmail);
    }

    @Override
    public List<OauthAccount> findOauthAccountsByUserId(Long userId) {
        return oauthAccountRepository.findByUserId(userId);
    }

}
