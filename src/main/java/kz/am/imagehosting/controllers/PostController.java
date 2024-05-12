package kz.am.imagehosting.controllers;

import kz.am.imagehosting.domain.Post;
import kz.am.imagehosting.dto.create.PostDto;
import kz.am.imagehosting.dto.update.PostUpdateDto;
import kz.am.imagehosting.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
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
    private String getAllPosts(Model model, Authentication auth) {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        return "post/all_posts";
    }

    @PostMapping(path="")
    public String addOne(@ModelAttribute("postDto") PostDto postDto,
                         Authentication auth) {
        postService.savePost(postDto);
        String username = auth.getName();
        String redirectUrl = (username != null) ? String.format("/%s/posts", username): "/posts";
        return "redirect:" + redirectUrl;
    }

    @GetMapping(path="/{id}")
    private String getPost(@PathVariable(value="id") UUID id, Model model) {
        model.addAttribute("post", postService.getPostById(id));
        return "post/post";
    }

    @GetMapping(path="/{id}/edit")
    private String getUpdatePost(@PathVariable(value="id") UUID id, Model model) {
        model.addAttribute("post", postService.getPostById(id));
        model.addAttribute("accesses", postService.getAllAccess());
        return "post/edit_post";
    }

    @PatchMapping(value = "/{id}")
    private String updatePost(@PathVariable(value="id") UUID id,
//                              @RequestParam(value="postName") String postName,
//                              @RequestParam(value="accessId") Integer accessId,
                              PostUpdateDto puDto,
                              RedirectAttributes redirectAttrs, Authentication auth){
        Post post = postService.getPostById(id);
        String oldPostName = post.getPostName();
        postService.updatePost(post, puDto);
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
