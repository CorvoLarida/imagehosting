package kz.am.imagehosting.service;

import kz.am.imagehosting.domain.AuthRole;
import kz.am.imagehosting.domain.AuthUser;
import kz.am.imagehosting.domain.Post;
import kz.am.imagehosting.domain.PostCollection;
import kz.am.imagehosting.repository.PostCollectionRepository;
import kz.am.imagehosting.repository.PostRepository;
import kz.am.imagehosting.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuthUserService {
    private final UserRepository userRepository;
    private final AuthRoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthUserService(UserRepository userRepository, AuthRoleService roleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    public List<AuthUser> findAllUsers(){
        return userRepository.findAll();
    }

    public AuthUser getUserByUsername(String username) {
        return userRepository.findUserByUsername(username).orElse(null);
    }

    public AuthUser saveUser(@NonNull AuthUser authUser) {
        return userRepository.save(authUser);
    }

    public AuthUser saveUser(String username, String nonEncriptedPassword) {
        AuthUser authUserAccount = new AuthUser();
        authUserAccount.setUsername(username);
        authUserAccount.setPassword(passwordEncoder.encode(nonEncriptedPassword));
        authUserAccount.setActive(true);
        Set<AuthRole> roles = new HashSet<>();
        roles.add(roleService.getRole("USER"));
        authUserAccount.setUserRoles(roles);
        return userRepository.save(authUserAccount);
    }

    public boolean isClientAuthenticated(Authentication authentication) {
        // System.out.println(authentication);
        // System.out.println(authentication.getDetails());
        // System.out.println(authentication.getCredentials());
        // System.out.println(authentication.getPrincipal());
        return !(authentication.getPrincipal() instanceof String);
    }

}
