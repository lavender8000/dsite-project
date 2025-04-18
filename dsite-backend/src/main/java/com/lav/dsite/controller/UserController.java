package com.lav.dsite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lav.dsite.common.Result;
import com.lav.dsite.dto.UserResponseDto;
import com.lav.dsite.entity.User;
import com.lav.dsite.enums.ResponseStatus;
import com.lav.dsite.service.UserService;
import com.lav.dsite.utils.ResultHandler;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users/{userId}/public")
    @ResponseBody
    public ResponseEntity<Result<UserResponseDto>> getUserPublic(@PathVariable Long userId) {

        User user = userService.findUserById(userId);
        
        if (user == null) {
            return ResultHandler.getResponseEntity(Result.error(ResponseStatus.USER_NOT_FOUND));
        }
        
        UserResponseDto userResponseDto = UserResponseDto.fromUser(user);

        Result<UserResponseDto> result = Result.success(userResponseDto);

        return ResultHandler.getResponseEntity(result);
        
    }

}
