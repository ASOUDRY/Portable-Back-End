package com.soudry.portable_back_end.other;
import org.springframework.stereotype.Component;

@Component
public class OtherFacade {

    private final OtherService otherService;

    public OtherFacade(OtherService otherService) {
        this.otherService = otherService;
    }

    public String connect() {
        return otherService.wasDetected();
    }
    
}
