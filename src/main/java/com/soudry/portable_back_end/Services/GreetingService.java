package com.soudry.portable_back_end.Services;

import org.springframework.stereotype.Service;

@Service
public class GreetingService {
    public String getGreeting() {
        return "Hello, world!";
    }

    public String greetByName(String name) {
        return "Hello, " + name + "!";
    }

    public String echo(String message) {
        return "You said: " + message;
    }
}