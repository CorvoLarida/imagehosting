package kz.am.imagehosting.controllers.user;

import kz.am.imagehosting.service.PostCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping(path="/{username}/collections")
public class UserCollectionsController {
    private final PostCollectionService pcService;

    @Autowired
    public UserCollectionsController(PostCollectionService pcService) {
        this.pcService = pcService;

    }
    @GetMapping("")
    private String getUserCollections(@PathVariable(value="username") String username,
                                      Model model, Authentication auth) {
        model.addAttribute("collections", pcService.getAllUserCollections(auth));
        return "user/collection/all_collections";
    }
    @GetMapping(path="/{id}")
    private String getUserCollection(@PathVariable(value="username") String username, @PathVariable(value="id") UUID id,
                                     Model model) {
        model.addAttribute("collection", pcService.getCollectionById(id));
        return "user/collection/collection";
    }
}
