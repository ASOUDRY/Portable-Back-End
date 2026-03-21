package com.soudry.portable_back_end.user.Services;
import org.springframework.stereotype.Service;
import com.soudry.portable_back_end.user.UserFacade;

@Service
public class FreeService {

    private final UserFacade userFacade;
    public FreeService(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    public String call() {
       return userFacade.connect();
    }
}