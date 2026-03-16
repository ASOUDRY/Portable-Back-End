package com.soudry.portable_back_end.Controllers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.soudry.portable_back_end.Services.GreetingService;

@RestController
@RequestMapping("/greetings")
public class GreetingController {

    private final GreetingService greetingService;

    public GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }


    @GetMapping
    public String getGreeting() {
        return "Hello, world!";
    }

    @GetMapping("/bear")
    public String getBear() {
        return "Hello, Bear";
    }

     // GET /api/greetings/{name}
    @GetMapping("/{name}")
    public String greetByName(@PathVariable String name) {
        return greetingService.echo(name);
    }

}