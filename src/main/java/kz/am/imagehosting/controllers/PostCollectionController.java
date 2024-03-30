package kz.am.imagehosting.controllers;

import kz.am.imagehosting.domain.PostCollection;
import kz.am.imagehosting.dto.PostCollectionDto;
import kz.am.imagehosting.service.PostCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @PostMapping(path="")
    private String addCollection(PostCollectionDto postCollectionDto,
                                 Authentication auth) {
        pcService.createCollection(postCollectionDto.getPostCollectionName(), postCollectionDto.getSelectedPosts());
        String username = auth.getName();
        if (username != null) return String.format("redirect:/%s/collections", username);
        return "redirect:/collections";
    }

    @GetMapping(path="/{id}")
    private String getCollection(@PathVariable(value="id") UUID id, Model model) {
        model.addAttribute("collection", pcService.getCollectionById(id));
        return "collection/collection";
    }

    @GetMapping(path="/{id}/edit")
    private String getUpdateCollection(@PathVariable(value="id") UUID id, Model model) {
        model.addAttribute("collection", pcService.getCollectionById(id));
        model.addAttribute("posts", pcService.getAllPosts());
        return "collection/edit_collection";
    }

    @PatchMapping(value = "/{id}")
    private String updateCollection(@PathVariable(value="id") UUID id,
                                    @RequestParam(value="postCollectionName") String postCollectionName,
                                    @RequestParam(value="selectedPosts", required = false) UUID[] selectedPosts,
                                    RedirectAttributes redirectAttrs, Authentication auth){
        PostCollection pc = pcService.getCollectionById(id);
        String oldName = pc.getPostCollectionName();
        pcService.updateCollection(pc, postCollectionName, selectedPosts);
        redirectAttrs.addAttribute("collectionUpdated", oldName);
        String redirectUrl = "/" + auth.getName() + "/collections";
        return "redirect:" + redirectUrl;
    }

    @DeleteMapping(path="/{id}")
    private String deleteCollection(@PathVariable(value="id") UUID id,
                              RedirectAttributes redirectAttrs, Authentication auth) {
        PostCollection pc = pcService.getCollectionById(id);
        pcService.deleteCollection(pc);
        redirectAttrs.addAttribute("collectionDeleted", pc.getPostCollectionName());
        String redirectUrl = "/" + auth.getName() + "/collections";
        return "redirect:" + redirectUrl;
    }

    @GetMapping(path="/new")
    private String createCollection(Model model) {
        model.addAttribute("posts", pcService.getAllPosts());
        return "collection/new_collection";
    }

}
