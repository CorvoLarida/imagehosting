package kz.am.imagehosting.controllers;

import jakarta.validation.Valid;
import kz.am.imagehosting.domain.AuthRole;
import kz.am.imagehosting.domain.AuthUser;
import kz.am.imagehosting.dto.RegistrationDto;
import kz.am.imagehosting.repository.RoleRepository;
import kz.am.imagehosting.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class AuthController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public AuthController(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(path="/login")
    public String getLogin(){
        return "/login";
    }
    @GetMapping(path="/register")
    public String getRegister(Model model){
        model.addAttribute("user",new RegistrationDto());
        return "/register";
    }
    @PostMapping(path="/register")
    public String register(@Valid @ModelAttribute("user") RegistrationDto rdto,
                           BindingResult bindingResult, Model model) {
        System.out.println("registering");
        AuthUser authUserAccount = userRepository.findUserByUsername(rdto.getUsername()).orElse(null);
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", rdto);
            return "register";
        }
        if (authUserAccount != null) {
            bindingResult.rejectValue("username", "There is a user with this username");
        } else {
            authUserAccount = new AuthUser();
            authUserAccount.setUsername(rdto.getUsername());
            authUserAccount.setPassword(passwordEncoder.encode(rdto.getPassword()));
            authUserAccount.setActive(true);
            Set<AuthRole> roles = new HashSet<>();
            roles.add(roleRepository.findRoleByName("USER").orElse(null));
            authUserAccount.setUserRoles(roles);
            System.out.println(authUserAccount);
            userRepository.save(authUserAccount);
        }
        return "redirect:/?registerSuccess";
    }
}
