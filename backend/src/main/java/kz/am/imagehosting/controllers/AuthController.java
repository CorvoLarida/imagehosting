package kz.am.imagehosting.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kz.am.imagehosting.domain.AuthRole;
import kz.am.imagehosting.domain.AuthUser;
import kz.am.imagehosting.dto.create.RegistrationDTO;
import kz.am.imagehosting.repository.RoleRepository;
import kz.am.imagehosting.repository.UserRepository;
import kz.am.imagehosting.service.AuthRoleService;
import kz.am.imagehosting.service.AuthUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashSet;
import java.util.Set;

@Controller
public class AuthController {
    private final UserRepository userRepository;
    private final AuthUserService userService;

    @Autowired
    public AuthController(
        UserRepository userRepository,
        AuthUserService userService
    ) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping(path="/login")
    public String getLogin(){
        return "login";
    }

    @GetMapping(path="/register")
    public String getRegister(Model model){
        model.addAttribute("registrationForm", new RegistrationDTO());
        return "register";
    }

    @PostMapping(path="/register")
    public String register(@Valid @ModelAttribute(value="registrationForm") RegistrationDTO rdto,
                           BindingResult bindingResult, Model model) {
        // System.out.println("register");
        AuthUser authUserAccount = userRepository.findUserByUsername(rdto.getUsername()).orElse(null);
        // System.out.println(bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", rdto);
            return "register";
        }
        if (authUserAccount != null) {
            bindingResult.rejectValue("username", "There is a user with this username");
        } else {
            Authentication auth;
            userService.saveUser(rdto.getUsername(), rdto.getPassword());

            auth = SecurityContextHolder.getContext().getAuthentication();
            // System.out.println(auth);

            return "redirect:/?registerSuccess";
        }
        return "redirect:/";
    }

    @PostMapping(path="/logout")
    private String getLogout(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }

}
