package kz.am.imagehosting.controllers;

import kz.am.imagehosting.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path="/users")
    private String getUsers(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "user/user/all_users";
    }

    @GetMapping(path="/{username}")
    private String getUser(@PathVariable String username, Model model) {
        model.addAttribute("username", username);
        model.addAttribute("posts", userService.findUserPosts(username));
        model.addAttribute("collections", userService.findUserCollections(username));
        return "user/user/user";
    }

}
