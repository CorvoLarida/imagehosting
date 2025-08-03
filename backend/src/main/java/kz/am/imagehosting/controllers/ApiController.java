package kz.am.imagehosting.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kz.am.imagehosting.domain.AuthRole;
import kz.am.imagehosting.domain.AuthUser;
import kz.am.imagehosting.dto.LoginDto;
import kz.am.imagehosting.dto.create.RegistrationDto;
import kz.am.imagehosting.repository.RoleRepository;
import kz.am.imagehosting.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping(path="/api")
public class ApiController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApiController(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(path="/csrf")
    private String csrf(CsrfToken token) {
        System.out.println(token);
        return "1";
    }

    @PostMapping(path="/login")
    private ResponseEntity<String> confirmLogin(@RequestBody LoginDto loginDto) {
        System.out.println(loginDto);
        System.out.println(loginDto.getUsername());
        System.out.println(loginDto.getPassword());
        AuthUser authUserAccount = userRepository.findUserByUsername(loginDto.getUsername()).orElse(null);
        if (authUserAccount != null) {
            return ResponseEntity.ok("ok");
        }
        return ResponseEntity.ok("NONONO");
    }

}
