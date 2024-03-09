package kz.am.imagehosting.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path="/{username}")
public class UserController {

//    @GetMapping(path="/posts")
//    @GetMapping(path="/posts/{id}")
//    @GetMapping(path="/posts/new")
    @GetMapping(path="/test")
    private String getUserPosts(Model model, Authentication auth) {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getDetails());
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getCredentials());
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        return "redirect:/";
    }


}
