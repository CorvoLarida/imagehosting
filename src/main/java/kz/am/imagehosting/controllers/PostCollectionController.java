package kz.am.imagehosting.controllers;

import kz.am.imagehosting.domain.Post;
import kz.am.imagehosting.domain.PostCollection;
import kz.am.imagehosting.repository.PostCollectionRepository;
import kz.am.imagehosting.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Controller
@RequestMapping(path="/collections")
public class PostCollectionController {
    private final PostRepository postRepository;
    private final PostCollectionRepository postCollectionRepository;

    @Autowired
    public PostCollectionController(PostRepository postRepository, PostCollectionRepository postCollectionRepository) {
        this.postRepository = postRepository;
        this.postCollectionRepository = postCollectionRepository;
    }

    @GetMapping("")
    private String getAllCollections(Model model) {
        model.addAttribute("collections", postCollectionRepository.findAll());
        return "collection/all_collections";
    }
    @GetMapping(path="/{id}")
    private String getCollection(@PathVariable("id") UUID id, Model model) {
        model.addAttribute("collection", postCollectionRepository.findById(id).orElse(null));
        return "collection/collection";
    }
    @GetMapping(path = "/new")
    private String createCollection(Model model) {
        model.addAttribute("posts", postRepository.findAll());
        return "collection/new_collection";
    }
    @PostMapping(path="")
    private String addCollection(@RequestParam(value="postCollectionName") String postCollectionName,
                         @RequestParam(value="selectedPosts") UUID[] selectedPosts) {
        PostCollection postCollection = new PostCollection();
        postCollection.setPostCollectionName(postCollectionName);

        Set<Post> posts = new HashSet<>();
        Set<PostCollection> postCollections = new HashSet<>();
        for (UUID post: selectedPosts){
            posts.add(postRepository.findById(post).orElse(null));
        }
        postCollection.setCollectionPosts(posts);
        postCollections.add(postCollection);
        postCollectionRepository.save(postCollection);

        System.out.println("Method done");

        return "redirect:/collections";
    }

}
