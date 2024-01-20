package kz.am.imagehosting.controllers;

import kz.am.imagehosting.domain.Image;
import kz.am.imagehosting.domain.Post;
import kz.am.imagehosting.repository.ImageRepository;
import kz.am.imagehosting.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping(path="/posts")
public class PostController {
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;

    @Autowired
    public PostController(PostRepository postRepository, ImageRepository imageRepository) {
        this.postRepository = postRepository;
        this.imageRepository = imageRepository;
    }

    @GetMapping(path="")
    private String getAllPosts(Model model) {
        model.addAttribute("posts", postRepository.findAll(Sort.by("createdAt").descending()));
        return "post/all_posts";
    }

    private static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/images";

    @PostMapping(path="")
    public String addOne(@RequestParam("postName") String postName,
                         @RequestParam("postImage") MultipartFile file) {

        StringBuilder fileNames = new StringBuilder();
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
        fileNames.append(file.getOriginalFilename());
        System.out.println(fileNames);
        try {
            Files.write(fileNameAndPath, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Image uploadedImage = new Image();
        uploadedImage.setImageLocation(fileNames.toString());
        imageRepository.save(uploadedImage);
        Post post = new Post();
        post.setPostName(postName);
        post.setImage(uploadedImage);
        postRepository.save(post);

        return "redirect:/posts";
    }

    @GetMapping(path="/{id}")
    private String getPost(@PathVariable("id") UUID id, Model model) {
        model.addAttribute("post", postRepository.findById(id).orElse(null));
        return "post/post";
    }

    @GetMapping(path = "/new")
    public String createOne() {
        return "post/new_post";
    }

}
