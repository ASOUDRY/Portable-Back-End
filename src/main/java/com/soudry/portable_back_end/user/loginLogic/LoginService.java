package com.soudry.portable_back_end.user.loginLogic;

import org.springframework.stereotype.Service;

import com.soudry.portable_back_end.auth.AuthFacade;
import com.soudry.portable_back_end.auth.tokens.AccessRefreshTokens;
import com.soudry.portable_back_end.user.repo.UserRepo;
import com.soudry.portable_back_end.user.repo.Users;

@Service
public class LoginService {

    private final AuthFacade authFacade;
    private final UserRepo userRepo;

    public LoginService(AuthFacade authFacade, UserRepo userRepo) {
        this.authFacade = authFacade;
        this.userRepo = userRepo;
    }

    public authorizedDto login(LoginDto dto) {
        AccessRefreshTokens art = authFacade.authenticate(dto.username(), dto.password());
        String id = userRepo.findByName(dto.username()).get().getId();
        return new authorizedDto(id, art.accessToken(), art.refreshToken());
    }
    
    public Users getUser(String username) {
        return userRepo.findByName(username).get();
    }

    public Users getUserbyId(String id) {
        return userRepo.findById(id).get();
    }
}