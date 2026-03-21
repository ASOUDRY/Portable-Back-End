package com.soudry.portable_back_end.user;

import org.springframework.stereotype.Component;

import com.soudry.portable_back_end.other.OtherFacade;

@Component
public class UserFacade {

    private final OtherFacade otherFacade;

    public UserFacade(OtherFacade otherFacade) {
        this.otherFacade = otherFacade;
    }
    public String connect() {
       return otherFacade.connect();
    }
}