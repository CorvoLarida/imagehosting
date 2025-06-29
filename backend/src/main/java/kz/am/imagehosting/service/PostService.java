package kz.am.imagehosting.service;

import kz.am.imagehosting.domain.AuthUser;
import kz.am.imagehosting.domain.Image;
import kz.am.imagehosting.domain.Post;
import kz.am.imagehosting.domain.PostAccess;
import kz.am.imagehosting.dto.create.PostDto;
import kz.am.imagehosting.dto.update.PostUpdateDto;
import kz.am.imagehosting.repository.AccessRepository;
import kz.am.imagehosting.repository.ImageRepository;
import kz.am.imagehosting.repository.PostRepository;
import kz.am.imagehosting.repository.UserRepository;
import kz.am.imagehosting.utils.ValidateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final AccessRepository accessRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/images";

    @Autowired
    public PostService(PostRepository postRepository, AccessRepository accessRepository, ImageRepository imageRepository,
                       UserRepository userRepository) {
        this.postRepository = postRepository;
        this.accessRepository = accessRepository;
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
    }
    boolean canUserSeePost(Post post, Authentication auth){
        if (post.getAccess().getType().equals("PUBLIC")) return true;
        if (auth == null) return false;
        return post.getCreatedBy().getUsername().equals(auth.getName()) ||
               auth.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
    }
    public List<PostAccess> getAllAccess(){
        return accessRepository.findAll();
    }

    public Post getPostById(UUID id){
        return postRepository.findById(id).orElse(null);
    }
    public List<Post> getAllPosts(){
        return postRepository.findAll(Sort.by("createdAt").descending());
    }
    public List<Post> getAllPosts(Authentication auth){
        List<Post> allPosts = this.getAllPosts();
        if (!allPosts.isEmpty()) return allPosts.stream().filter(post -> this.canUserSeePost(post, auth)).collect(Collectors.toList());
        return allPosts;
    }

    public List<Post> getAllPosts(Authentication auth, String username){
        AuthUser user = userRepository.findUserByUsername(username).orElse(null);
        if (user != null) return postRepository.findPostsByCreatedByOrderByCreatedAtDesc(user).stream()
                .filter(post -> this.canUserSeePost(post, auth)).collect(Collectors.toList());
        return Collections.emptyList();
    }

    public void deletePost(Post post){
        if (post == null) throw new RuntimeException("Post must not be null");
        postRepository.deleteById(post.getId());
    }

    public void savePost(PostDto postDto) {
        ValidateUtils.validatePostDto(postDto);
        String postName = postDto.getPostName();
        MultipartFile file = postDto.getPostImage();
        Integer accessId = postDto.getAccessId();
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
        Post post = new Post();
        post.setPostName(postName);
        post.setImage(uploadedImage);
        post.setAccess(accessRepository.getReferenceById(accessId));
        post.setCreatedBy(userRepository.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
                ).orElse(null));
        postRepository.save(post);
    }

    public void updatePost(Post post, PostUpdateDto puDto){
        ValidateUtils.validatePostUpdateDto(puDto);
        post.setPostName(puDto.getPostName());
        post.setAccess(accessRepository.getReferenceById(puDto.getAccessId()));
        postRepository.save(post);
    }

}
