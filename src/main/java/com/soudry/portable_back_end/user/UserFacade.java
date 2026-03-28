package com.soudry.portable_back_end.user;

import org.springframework.stereotype.Component;
import com.soudry.portable_back_end.user.repo.Users;
import com.soudry.portable_back_end.user.loginLogic.LoginService;
@Component
public class UserFacade {

    private final LoginService loginService;

    public UserFacade(LoginService loginService) {
        this.loginService = loginService;
    }

    public Users retreiveUser(String username) {
         return loginService.getUser(username);
    }

    public Users retreiveUserById(String id) {
         return loginService.getUserbyId(id);
    }
}