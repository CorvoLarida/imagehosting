package kz.am.imagehosting.controllers;

import kz.am.imagehosting.domain.Post;
import kz.am.imagehosting.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping(path="/posts")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping(path="")
    private String getAllPosts(Model model) {
        model.addAttribute("posts", postService.findAllPosts());
        return "post/all_posts";
    }

    @PostMapping(path="")
    public String addOne(@RequestParam("postName") String postName,
                         @RequestParam("postImage") MultipartFile file,
                         Authentication auth) {
        postService.savePost(postName, file);
        String username = auth.getName();
        if (username != null) return String.format("redirect:/%s/posts", username);
        return "redirect:/posts";
    }

    @GetMapping(path="/{id}")
    private String getPost(@PathVariable("id") UUID id, Model model) {
        model.addAttribute("post", postService.findPostById(id));
        return "post/post";
    }
    @GetMapping(path="/delete/{id}")
    private String deletePost(@PathVariable("id") UUID id, Model model,
                              RedirectAttributes redirectAttrs, Authentication auth) {
        Post post = postService.findPostById(id);
        postService.deletePost(post);
        redirectAttrs.addAttribute("postDeleted", post.getPostName());
        String redirectUrl = "/" + auth.getName() + "/posts";
        System.out.println(redirectUrl);
        return "redirect:" + redirectUrl;
    }

    @GetMapping(path="/new")
    public String createOne() {
        return "post/new_post";
    }

}
