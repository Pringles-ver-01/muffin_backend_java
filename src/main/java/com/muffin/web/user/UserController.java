package com.muffin.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {


    @Autowired UserService userService;


    @PostMapping("/signUp")
    public void save(@RequestBody User user){
        System.out.println(user);
        userService.save(user);
    }

    @GetMapping("/csv")
    public void readCsv() {userService.readCSV();}
}
