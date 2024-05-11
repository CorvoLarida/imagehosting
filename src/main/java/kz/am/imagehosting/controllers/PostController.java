package kz.am.imagehosting.controllers;

import jakarta.servlet.http.HttpServletRequest;
import kz.am.imagehosting.domain.Post;
import kz.am.imagehosting.dto.PostDto;
import kz.am.imagehosting.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.http.HttpRequest;
import java.util.Enumeration;
import java.util.UUID;
import java.util.stream.Stream;

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
        model.addAttribute("posts", postService.getAllPublicPosts());
        return "post/all_posts";
    }

    @PostMapping(path="")
    public String addOne(@ModelAttribute("postDto") PostDto postDto,
                         Authentication auth) {
        postService.savePost(postDto);
        String username = auth.getName();
        if (username != null) return String.format("redirect:/%s/posts", username);
        return "redirect:/posts";
    }

    @GetMapping(path="/{id}")
    private String getPost(@PathVariable(value="id") UUID id, Model model) {
        model.addAttribute("post", postService.getPostById(id));
        return "post/post";
    }

    @GetMapping(path="/{id}/edit")
    private String getUpdatePost(@PathVariable(value="id") UUID id, Model model) {
        model.addAttribute("post", postService.getPostById(id));
        return "post/edit_post";
    }

    @PatchMapping(value = "/{id}")
    private String updatePost(@PathVariable(value="id") UUID id,
                              @RequestParam(value="postCollectionName") String postName,
                              RedirectAttributes redirectAttrs, Authentication auth){
        Post post = postService.getPostById(id);
        String oldPostName = post.getPostName();
        postService.updatePost(post, postName);
        redirectAttrs.addAttribute("postUpdated", oldPostName);
        String redirectUrl = "/" + auth.getName() + "/posts";
        return "redirect:" + redirectUrl;
    }

    @DeleteMapping(path="/{id}")
    private String deletePost(@PathVariable(value="id") UUID id,
                              RedirectAttributes redirectAttrs, Authentication auth) {
        Post post = postService.getPostById(id);
        postService.deletePost(post);
        redirectAttrs.addAttribute("postDeleted", post.getPostName());
        String redirectUrl = "/" + auth.getName() + "/posts";
        return "redirect:" + redirectUrl;
    }

    @GetMapping(path="/new")
    public String createOne(Model model) {
        model.addAttribute("accesses", postService.getAllAccess());
        return "post/new_post";
    }

}
