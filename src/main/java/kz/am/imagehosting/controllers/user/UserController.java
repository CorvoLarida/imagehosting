package kz.am.imagehosting.controllers.user;

import kz.am.imagehosting.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path="/{username}")
public class UserController {

    private final PostService postService;

    @Autowired
    public UserController(PostService postService) {
        this.postService = postService;
    }
    @GetMapping(path="/test")
    private String testUser(Model model, Authentication auth) {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getDetails());
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getCredentials());
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        System.out.printf("%s%n",auth.getName());
        return "redirect:/";
    }
}
