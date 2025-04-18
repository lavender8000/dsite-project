package com.lav.dsite.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lav.dsite.dto.OauthAccountResponseDto;
import com.lav.dsite.dto.UpdateUserInfoDto;
import com.lav.dsite.dto.UpdateUserPasswordDto;
import com.lav.dsite.dto.UserAccountProfileDto;
import com.lav.dsite.dto.UserOauthRegisterDto;
import com.lav.dsite.dto.UserPasswordRegisterDto;
import com.lav.dsite.entity.User;
import com.lav.dsite.entity.OauthAccount;
import com.lav.dsite.enums.ResponseStatus;
import com.lav.dsite.exception.UserServiceException;
import com.lav.dsite.repository.UserRepository;
import com.lav.dsite.service.JwtTokenService;
import com.lav.dsite.service.OauthAccountService;
import com.lav.dsite.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OauthAccountService oauthAccountService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenService jwtTokenService;
    
    @Override
    public User findUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public User saveUser(User user) {
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new UserServiceException(ResponseStatus.USER_EMAIL_EXISTS, e);
        } catch (OptimisticLockingFailureException e) {
            throw new UserServiceException(ResponseStatus.OPTIMISTIC_LOCK_EXCEPTION, e);
        }
    }

    // 一定會返回用戶，不會返回 null，但可能會拋出異常
    @Transactional(rollbackFor = Exception.class)
    @Override
    public User registerUserWithPassword(UserPasswordRegisterDto user) {

        // 檢查該email是否已經存在於 oauth_account 表中，代表該email已經被綁定於某帳戶
        OauthAccount oauthAccount = oauthAccountService.findOauthAccountByOauthEmail(user.getEmail());
        if (oauthAccount != null) {
            throw new UserServiceException(ResponseStatus.USER_EMAIL_EXISTS);
        }
        
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setNickName(user.getNickName().trim());

        return saveUser(newUser);

    }

    // 一定會返回用戶，不會返回 null，但可能會拋出異常
    @Transactional(rollbackFor = Exception.class)
    @Override
    public User registerUserWithOauth(UserOauthRegisterDto userOauthRegisterDto) {

        User newUser = new User();
        newUser.setEmail(userOauthRegisterDto.getOauthEmail());
        newUser.setNickName(userOauthRegisterDto.getNickName().trim());

        // 儲存新用戶
        User savedUser = saveUser(newUser);

        OauthAccount newOauthAccount = new OauthAccount();
        newOauthAccount.setUser(savedUser);
        newOauthAccount.setOauthProvider(userOauthRegisterDto.getOauthProvider());
        newOauthAccount.setOauthId(userOauthRegisterDto.getOauthId());
        newOauthAccount.setOauthEmail(userOauthRegisterDto.getOauthEmail());

        // 將新的 OauthAccounts 資訊儲存到該用戶的 OauthAccount 中
        savedUser.getOauthAccounts().add(newOauthAccount);
        
        // User 中的 CascadeType.ALL 會自動同步到 oauth_account 表中
        return saveUser(savedUser);
        
    }

    @Override
    public UserAccountProfileDto getUserAccountProfile(Long userId) {

        User user = findUserById(userId);

        if (user == null) {
            throw new UserServiceException(ResponseStatus.USER_NOT_FOUND);
        }

        boolean isPasswordSet = user.getPassword() != null && !user.getPassword().isEmpty();

        List<OauthAccount> oauthAccounts = oauthAccountService.findOauthAccountsByUserId(userId);
        List<OauthAccountResponseDto> oauthAccountResponseDtos = oauthAccounts.stream()
                .map(OauthAccountResponseDto::fromOauthAccount)
                .toList();
        
        UserAccountProfileDto userAccountProfileDto = new UserAccountProfileDto();
        userAccountProfileDto.setPasswordSet(isPasswordSet);
        userAccountProfileDto.setOauthAccounts(oauthAccountResponseDtos);

        return userAccountProfileDto;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserPassword(Long userId, UpdateUserPasswordDto updateUserPasswordDto) {

        User user = findUserById(userId);

        if (user == null) {
            throw new UserServiceException(ResponseStatus.USER_NOT_FOUND);
        }

        if (updateUserPasswordDto.getCurrentPassword() == null || updateUserPasswordDto.getCurrentPassword().isEmpty()) {
            // 新增密碼
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                throw new UserServiceException(ResponseStatus.USER_PASSWORD_ALREADY_SET);
            }
            user.setPassword(passwordEncoder.encode(updateUserPasswordDto.getNewPassword()));
            saveUser(user);
        } else {
            // 修改密碼
            if (!passwordEncoder.matches(updateUserPasswordDto.getCurrentPassword(), user.getPassword())) {
                throw new UserServiceException(ResponseStatus.USER_PASSWORD_NOT_MATCHED);
            }
            user.setPassword(passwordEncoder.encode(updateUserPasswordDto.getNewPassword()));
            saveUser(user);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserInfo(HttpServletRequest request, HttpServletResponse response, Long userId, UpdateUserInfoDto updateUserInfoDto) {
        
        User user = findUserById(userId);

        if (user == null) {
            throw new UserServiceException(ResponseStatus.USER_NOT_FOUND);
        }

        if (updateUserInfoDto.getNickName() != null && !updateUserInfoDto.getNickName().isEmpty()) {
            user.setNickName(updateUserInfoDto.getNickName().trim());
        }
        
        User savedUser = saveUser(user);

        // 修改 jwt 中的用戶資訊，並更新 cookie 中的 JWT
        String jwt = jwtTokenService.getJwtFromCookie(request);
        if (jwt == null) {
            throw new UserServiceException(ResponseStatus.INVALID_JWT_TOKEN);
        }
        String updateAccessToken = jwtTokenService.updateAccessToken(jwt, savedUser.getNickName());
        jwtTokenService.addJwtToCookie(updateAccessToken, response);
    }

}
