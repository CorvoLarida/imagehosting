package kz.am.imagehosting.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PostCollectionsController {

    @GetMapping("/collections")
    private String getAllPosts() {
        return "collection/all_collections";
    }
}
