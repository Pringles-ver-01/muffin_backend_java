package com.muffin.web.user;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final MailService mailService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService, MailService mailService) {
        this.userService = userService;
        this.mailService = mailService;
    }

    @GetMapping("/csv")
    public void readCsv(){
        userService.readCsv();
    }

    @GetMapping("/idCheck/{emailId}")
    public boolean idCheck(@PathVariable String emailId) {
        System.out.println(emailId);
        return userService.exists(emailId);
    }

    @PostMapping("/signUp")
    public ResponseEntity<User> save(@RequestBody User user) {
        System.out.println(user);
        return ResponseEntity.ok(userService.save(user));
    }

    @PostMapping("/signIn")
    public ResponseEntity<Map<?,?>> login(@RequestBody User user) {
        System.out.println(user);
        Optional<User> findUser = userService.findByEmailId(user.getEmailId());

        if (findUser.isPresent()) {
            User returnUser = findUser.get();
            Map<String ,Object> box = new HashMap<>();
            box.put("loginedUser", returnUser);
            box.put("usersAsset", userService.findUsersAsset((long) 1));
            return returnUser.getPassword().equals(user.getPassword()) ? ResponseEntity.ok(box) : ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/update")
    public ResponseEntity<User> update(@RequestBody User user) {
        System.out.println(user.getUserId());
        Optional<User> findUser = userService.findByUserId(user.getUserId());
        if (findUser.isPresent()) {
            User returnUser = findUser.get();
            Optional.ofNullable(user.getPassword()).ifPresent(returnUser::setPassword);
            Optional.ofNullable(user.getNickname()).ifPresent(returnUser::setNickname);
            return ResponseEntity.ok(userService.save(returnUser));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<User> delete(@RequestBody User user) {
        Optional<User> findUser = userService.findByUserId(user.getUserId());
        if(findUser.isPresent()) {
            System.out.println(findUser.get());
            userService.delete(findUser.get());
            System.out.println(findUser.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/findPassword/{emailId}")
    public ResponseEntity<User> findPassword(@PathVariable String emailId) {
        Optional<User> findUser = userService.findByEmailId(emailId);
        if (findUser.isPresent()) {
            User returnUser = findUser.get();
            returnUser.setPassword(mailService.init());
            userService.save(returnUser);
            mailService.mailSend(returnUser);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
