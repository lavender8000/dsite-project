package com.lav.dsite.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lav.dsite.entity.User;
import com.lav.dsite.service.UserService;

@Service
public class DsiteUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findUserByEmail(username);
        
        // 如果找不到 拋出異常
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // TODO: 權限信息 - 讀取並封裝，封裝權限信息，添加到 DsiteUserDetails 中，或修改 DsiteUserDetails 中的 getAuthorities 方法

        // 封裝成 DsiteUserDetails 並返為
        return new DsiteUserDetails(user);
    }

}
