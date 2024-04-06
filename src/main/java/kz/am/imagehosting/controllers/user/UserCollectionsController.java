package kz.am.imagehosting.controllers.user;

import jakarta.servlet.http.HttpServletResponse;
import kz.am.imagehosting.domain.Post;
import kz.am.imagehosting.domain.PostCollection;
import kz.am.imagehosting.service.PostCollectionService;
import kz.am.imagehosting.utils.ImageUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Controller
@RequestMapping(path="/{username}/collections")
public class UserCollectionsController {
    private final PostCollectionService pcService;

    @Autowired
    public UserCollectionsController(PostCollectionService pcService) {
        this.pcService = pcService;

    }

    @GetMapping(path="")
    private String getUserCollections(@PathVariable(value="username") String username,
                                      Model model, Authentication auth) {
        model.addAttribute("collections", pcService.getAllUserCollections(auth));
        return "user/collection/all_collections";
    }

    @GetMapping(path="/{id}")
    private String getUserCollection(@PathVariable(value="username") String username, @PathVariable(value="id") UUID id,
                                     Model model, Authentication auth) {
        PostCollection pc = pcService.getCollectionById(id);
        boolean canDelete = false;
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
        if (auth.getName().equals(pc.getCreatedBy().getUsername()) || isAdmin) canDelete = true;
        model.addAttribute("collection", pc);
        model.addAttribute("canDelete", canDelete);
        return "user/collection/collection";
    }
    @GetMapping(path="/{id}/download")
    private void downloadUserCollection(@PathVariable String username, @PathVariable(value="id") UUID id,
                                        HttpServletResponse response){
        response.setContentType("application/zip");
        PostCollection pc = pcService.getCollectionById(id);
        response.setHeader("Content-Disposition",
                String.format("attachment; filename=%s.zip", pc.getPostCollectionName()));
        Set<Post> posts = pc.getCollectionPosts();
        try (ZipOutputStream zippedOut = new ZipOutputStream(response.getOutputStream())) {
            for (Post post : posts) {
                String imageName = post.getImage().getImageLocation();
                FileSystemResource resource = new FileSystemResource(ImageUtils.getImagePath(imageName));
                String imageExt = ImageUtils.getImageExtenstion(imageName);
                ZipEntry entry = new ZipEntry(String.format("%s.%s", post.getPostName(), imageExt));
                entry.setSize(resource.contentLength());
                zippedOut.putNextEntry(entry);
                IOUtils.copy(resource.getInputStream(), zippedOut);
                zippedOut.closeEntry();
            }
            zippedOut.finish();
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("IOError writing file to output stream");
        }
    }

}
