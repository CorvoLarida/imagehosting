package kz.am.imagehosting.controllers;

import kz.am.imagehosting.domain.User;
import kz.am.imagehosting.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String getRegister(){
        return "/register";
    }
    @PostMapping("/register")
    public String register(@RequestParam("username") String username, @RequestParam("password") String password) {
        User userAccount = new User();
        userAccount.setUsername(username);
        userAccount.setPassword(passwordEncoder.encode(password));
        userAccount.setActive(true);
        userRepository.save(userAccount);
        return "redirect:/login";
    }
}
