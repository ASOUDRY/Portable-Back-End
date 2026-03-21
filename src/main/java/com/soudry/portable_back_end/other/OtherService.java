package com.soudry.portable_back_end.other;
import org.springframework.stereotype.Service;

@Service
public class OtherService {
    protected String wasDetected() {
        return "This is from the Other Module";
    }
}