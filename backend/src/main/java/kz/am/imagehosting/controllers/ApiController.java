package kz.am.imagehosting.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kz.am.imagehosting.controllers.error.UnauthorizedAccessException;
import kz.am.imagehosting.controllers.error.UserAlreadyRegisteredException;
import kz.am.imagehosting.domain.AuthRole;
import kz.am.imagehosting.domain.AuthUser;
import kz.am.imagehosting.dto.LoginDTO;
import kz.am.imagehosting.dto.RoleDTO;
import kz.am.imagehosting.dto.UserDTO;
import kz.am.imagehosting.dto.create.RegistrationDTO;
import kz.am.imagehosting.repository.RoleRepository;
import kz.am.imagehosting.repository.UserRepository;
import kz.am.imagehosting.service.AuthUserService;
import software.amazon.awssdk.http.HttpStatusCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import java.util.Iterator;

@RestController
@RequestMapping(path="/api")
public class ApiController {

    private final AuthUserService userService;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    private final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    @Autowired
    public ApiController(
        AuthUserService userService,
        AuthenticationManager authenticationManager,
        SecurityContextRepository securityContextRepository
    ){
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

    @GetMapping(path="/csrf")
    private String csrf(CsrfToken token) {
        // System.out.println(token);
        return "1";
    }

    @PostMapping(path="/login")
    private Object confirmLogin(
        @RequestBody LoginDTO loginDTO,
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        // System.out.println(loginDTO);
        // System.out.println(loginDTO.username());
        // System.out.println(loginDTO.password());
        String username = loginDTO.username();
        AuthUser authUserAccount = userService.getUserByUsername(username);
        if (authUserAccount == null) {
            throw new UsernameNotFoundException("User not found");
        }
        UsernamePasswordAuthenticationToken authToken = UsernamePasswordAuthenticationToken.unauthenticated(
            username,
            loginDTO.password()
        );
        Authentication authentication = this.authenticationManager.authenticate(authToken);
        SecurityContext sc = this.securityContextHolderStrategy.createEmptyContext();
        sc.setAuthentication(authentication);
        this.securityContextHolderStrategy.setContext(sc);
        this.securityContextRepository.saveContext(sc, request, response);

        UserDTO userDTO = new UserDTO().mapForJson(authUserAccount);
        // System.out.println(userDTO);
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @PostMapping(path="/register")
    @ResponseBody
    private ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationDTO rDTO,
                                HttpServletRequest request, HttpServletResponse response, HttpSession session
    ) {
        // System.out.println(rDTO);
        // System.out.println(rDTO.getUsername());
        // System.out.println(rDTO.getPassword());

        String username = rDTO.getUsername();
        String password = rDTO.getPassword();
        AuthUser authUserAccount = userService.getUserByUsername(username);
        if (authUserAccount != null) {
            throw new UserAlreadyRegisteredException("User already exists");
        }
        Authentication auth;
        authUserAccount = userService.saveUser(username, password);

        // session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, sc);

        auth = SecurityContextHolder.getContext().getAuthentication();
        // System.out.println(auth);

        // UserDTO userDTO = new UserDTO().mapForJson(authUserAccount);
        // // System.out.println(userDTO);
        return ResponseEntity.status(HttpStatusCode.CREATED).body(null);
    }

    @PostMapping(path="/logout")
    private ResponseEntity<?> getLogout(
        HttpServletRequest request,
        HttpServletResponse response,
        HttpSession session
    ){
        // System.out.println("api logout");
        // System.out.println(session.getAttributeNames());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (userService.isClientAuthenticated(auth)){
            this.logoutHandler.logout(request, response, auth);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        throw new UnauthorizedAccessException("Not a user");
    }

    @PostMapping(path="/session")
    private ResponseEntity<?> getSession(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        // System.out.println("api session");
        // System.out.println(session.getAttributeNames());
        // Iterator<String> iterator = session.getAttributeNames().asIterator();
        // for (;iterator.hasNext();) {
        //     System.out.println(iterator.next());
        // }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        final class showAuthDetails {
            private static void printStuff(Object stuff) {
                System.out.println("printStuff");
                System.out.println(stuff);
                if (stuff != null) System.out.println(stuff.getClass());
            }
        }

        // System.out.println(auth);
        // showAuthDetails.printStuff(auth.getName());
        // showAuthDetails.printStuff(auth.getCredentials());
        // showAuthDetails.printStuff(auth.getPrincipal());
        // showAuthDetails.printStuff(auth.getAuthorities());

        if (userService.isClientAuthenticated(auth)) {
            String username = auth.getName();
            AuthUser user = userService.getUserByUsername(username);
            if (user == null) {
                this.logoutHandler.logout(request, response, auth);
            } else {
                UserDTO userDTO = new UserDTO().mapForJson(user);
                return ResponseEntity.status(HttpStatus.OK).body(userDTO);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body("getSession");
    }

}
