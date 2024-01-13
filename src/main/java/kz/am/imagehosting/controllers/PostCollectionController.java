package kz.am.imagehosting.controllers;

import jakarta.servlet.http.HttpServletRequest;
import kz.am.imagehosting.domain.Image;
import kz.am.imagehosting.domain.Post;
import kz.am.imagehosting.repository.PostCollectionRepository;
import kz.am.imagehosting.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
    public String createCollection(Model model) {
        model.addAttribute("posts", postRepository.findAll());
        return "collection/new_collection";
    }
    @PostMapping(path="")
    public String addOne(HttpServletRequest request) {
//    public String addOne(String postCollectionName, List<String> postsNames) {
//        System.out.println(postCollectionName);
//        System.out.println(postsNames);
        System.out.println(request);
        Map<String, String[]> paramMap = request.getParameterMap();
        for (String mapKey: paramMap.keySet()){
            System.out.println(mapKey + "=" + Arrays.toString(paramMap.get(mapKey)));
        }
        return "redirect:/collections";
    }

}
