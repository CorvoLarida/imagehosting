package kz.am.imagehosting.controllers;

import jakarta.servlet.http.HttpServletRequest;
import kz.am.imagehosting.domain.Image;
import kz.am.imagehosting.domain.Post;
import kz.am.imagehosting.repository.ImageRepository;
import kz.am.imagehosting.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Controller
public class PostsController {
    private PostRepository postRepository;
    private ImageRepository imageRepository;

    @Autowired
    public PostsController(PostRepository postRepository, ImageRepository imageRepository) {
        this.postRepository = postRepository;
        this.imageRepository = imageRepository;
    }



    @GetMapping("/posts")
    private String getAllPosts(Model model) {
        model.addAttribute("posts", postRepository.findAll());
        return "all_posts";
    }


    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/images";

    @PostMapping("/posts")
    public String addOne(@RequestParam("postName") String postName,
                         @RequestParam("postImage") MultipartFile file,
                         Model model) {

        StringBuilder fileNames = new StringBuilder();
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
        fileNames.append(file.getOriginalFilename());

        try {
            Files.write(fileNameAndPath, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Image uploadedImage = new Image();
        uploadedImage.setImageLocation(fileNames.toString());
        imageRepository.save(uploadedImage);
//        model.addAttribute("msg", "Uploaded images: " + fileNames);
//
//
//
//        if (bindingResult.hasErrors()) return "new_post";
        Post post = new Post();
        post.setPostName(postName);
        post.setImage(uploadedImage);
        postRepository.save(post);

        return "redirect:/posts";
    }

    @GetMapping("/posts/{id}")
    private String getPost(@PathVariable("id") long id, Model model) {
        model.addAttribute("post", postRepository.findById(id));
        return "post";
    }

    @GetMapping(value = "/posts/new")
    public String createOne(@ModelAttribute("post") Post post) {
        return "new_post";
    }

}
