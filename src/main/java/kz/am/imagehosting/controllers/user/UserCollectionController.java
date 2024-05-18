package kz.am.imagehosting.controllers.user;

import jakarta.servlet.http.HttpServletResponse;
import kz.am.imagehosting.domain.Post;
import kz.am.imagehosting.domain.PostCollection;
import kz.am.imagehosting.service.PostCollectionService;
import kz.am.imagehosting.utils.ImageUtils;
import kz.am.imagehosting.utils.UserUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
@RequestMapping(path="/{username}/collections")
public class UserCollectionController {
    private final PostCollectionService pcService;

    @Autowired
    public UserCollectionController(PostCollectionService pcService) {
        this.pcService = pcService;

    }

    @GetMapping(path="")
    private String getUserCollections(@PathVariable(value="username") String username,
                                      Model model, Authentication auth) {
        model.addAllAttributes(Map.of(
                "username", username,
                "collections", pcService.getAllCollections(username)
        ));
        return "collection/user/all_collections";
    }

    @GetMapping(path="/{id}")
    private String getUserCollection(@PathVariable(value="username") String username,
                                     @PathVariable(value="id") UUID id,
                                     Model model, Authentication auth) {
        PostCollection pc = pcService.getCollectionById(id);
        model.addAllAttributes(Map.of(
                "collection", pc,
                "collectionPosts", pcService.getCollectionPosts(auth, pc),
                "canDelete", UserUtils.canSeeAllUserPosts(auth, pc.getCreatedBy().getUsername())
        ));
        return "collection/user/collection";
    }
    @GetMapping(path="/{id}/download")
    private void downloadUserCollection(@PathVariable String username, @PathVariable(value="id") UUID id,
                                        HttpServletResponse response){
        response.setContentType("application/zip");
        PostCollection pc = pcService.getCollectionById(id);
        response.setHeader("Content-Disposition",
                String.format("attachment; filename=%s.zip", pc.getPostCollectionName()));
        Set<Post> posts = pc.getCollectionPosts();
        String imageName = null;
        FileSystemResource resource = null;
        String imageExt = null;
        try (ZipOutputStream zippedOut = new ZipOutputStream(response.getOutputStream())) {
            for (Post post : posts) {
                imageName = post.getImage().getImageLocation();
                resource = new FileSystemResource(ImageUtils.getImagePath(imageName));
                imageExt = ImageUtils.getImageExtension(imageName);
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
