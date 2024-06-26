package kz.am.imagehosting.controllers.user;

import jakarta.servlet.http.HttpServletResponse;
import kz.am.imagehosting.domain.Post;
import kz.am.imagehosting.service.PostService;
import kz.am.imagehosting.utils.ImageUtils;
import kz.am.imagehosting.utils.UserUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.*;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping(path="/{username}/posts")
public class UserPostController {
    private final PostService postService;

    @Autowired
    public UserPostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping(path="")
    private String getUserPosts(@PathVariable(value="username") String username,
                                Model model, Authentication auth) {
        model.addAllAttributes(Map.of(
                "username", username,
                "posts", postService.getAllPosts(auth, username)
        ));
        return "post/user/all_posts";
    }

    @GetMapping(path="/{id}")
    private String getUserPost(@PathVariable(value="username") String username, @PathVariable(value="id") UUID id,
                               Model model, Authentication auth) {
        Post post = postService.getPostById(id);
        model.addAllAttributes(Map.of(
                "post", post,
                "canDelete", UserUtils.canSeeAllUserPosts(auth, post.getCreatedBy().getUsername()),
                "userHasAccess",  UserUtils.canUserAccessPost(auth, post)
        ));
        return "post/user/post";
    }

    @GetMapping(path="/{id}/download")
    private void downloadUserPost(@PathVariable String username, @PathVariable(value="id") UUID id,
                                  HttpServletResponse response) {
        response.setContentType("image/jpeg");
        Post post = postService.getPostById(id);
        String imageName = post.getImage().getImageLocation();
        String imageExt = ImageUtils.getImageExtension(imageName);
        response.setHeader("Content-Disposition",
                String.format("attachment; filename=%s.%s", post.getPostName(), imageExt));
        String imageFilePath = ImageUtils.getImagePath(imageName);
        try(InputStream is = new FileInputStream(imageFilePath);
            OutputStream os = response.getOutputStream()) {
            IOUtils.copy(is, os);
            response.flushBuffer();
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("IOError writing file to output stream");
        }
    }

}
