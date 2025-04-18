package com.lav.dsite.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.lav.dsite.entity.User;

public class DsiteUserDetails implements UserDetails {

    private User user;

    public DsiteUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO: 權限信息 - 封裝，封裝權限信息(從Mysql讀取的User Entity中獲取)
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    public User getUser() {
        return user;
    }

}
