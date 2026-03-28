package com.soudry.portable_back_end.user.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.soudry.portable_back_end.user.registerLogic.RegisterService;
import com.soudry.portable_back_end.auth.AuthFacade;
import com.soudry.portable_back_end.user.loginLogic.LoginDto;
import com.soudry.portable_back_end.user.loginLogic.LoginService;
import com.soudry.portable_back_end.user.loginLogic.RefreshDto;
import com.soudry.portable_back_end.user.loginLogic.authorizedDto;
import com.soudry.portable_back_end.user.registerLogic.RegisterDto;

@RestController
@RequestMapping("/public")
public class UserController {

    private final RegisterService registerService;
    private final LoginService loginService;
    private final AuthFacade authFacade;
 
    public UserController( RegisterService registerService, LoginService loginService, AuthFacade authFacade)
    {
        this.loginService = loginService;
        this.registerService = registerService;
        this.authFacade = authFacade;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto dto) {
        if (registerService.accountExists(dto.username())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Account already exists");
        }
        String username = registerService.register(dto);
        String response = String.format("Thank you for registering with %s", username);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<authorizedDto> login(@RequestBody LoginDto dto) {
        authorizedDto returnDto = loginService.login(dto);
        return ResponseEntity.ok(returnDto);
    }
  
    @PostMapping("/refreshToken")
     public ResponseEntity<String> login(@RequestBody RefreshDto dto) {
         String newToken = authFacade.refreshAccessToken(dto);
        // authorizedDto returnDto = loginService.login(dto);
        return ResponseEntity.ok(newToken);
    }

    // @PostMapping("/logout")
    // public ResponseEntity<String> login(@RequestBody RefreshDto dto) {
    //      String newToken = authFacade.refreshAccessToken(dto);
    //     // authorizedDto returnDto = loginService.login(dto);
    //     return ResponseEntity.ok(newToken);
    // }
}