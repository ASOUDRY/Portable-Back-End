package com.soudry.portable_back_end.Controllers;

// import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private")
public class PrivateController {

    @GetMapping("/locked")
    public String locked() {
        return "You are authorized";
    }

    // @GetMapping("/get/{name}")
    // public ResponseEntity<Users>
    
}
