package kz.am.imagehosting.controllers;


import kz.am.imagehosting.service.PostCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping(path="/collections")
public class PostCollectionController {
    private final PostCollectionService pcService;

    @Autowired
    public PostCollectionController(PostCollectionService pcService) {
        this.pcService = pcService;
    }

    @GetMapping(path="")
    private String getAllCollections(Model model) {
        model.addAttribute("collections", pcService.getAllCollections());
        return "collection/all_collections";
    }
    @GetMapping(path="/{id}")
    private String getCollection(@PathVariable("id") UUID id, Model model) {
        model.addAttribute("collection", pcService.getCollectionById(id));
        return "collection/collection";
    }
    @GetMapping(path="/new")
    private String createCollection(Model model) {
        model.addAttribute("posts", pcService.getAllPosts());
        return "collection/new_collection";
    }
    @PostMapping(path="")
    private String addCollection(@RequestParam(value="postCollectionName") String postCollectionName,
                                 @RequestParam(value="selectedPosts") UUID[] selectedPosts,
                                 Authentication auth) {
        pcService.createCollection(postCollectionName, selectedPosts);
        String username = auth.getName();
        if (username != null) return String.format("redirect:/%s/collections", username);
        return "redirect:/collections";
    }

}
