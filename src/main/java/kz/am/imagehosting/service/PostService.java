package kz.am.imagehosting.service;

import kz.am.imagehosting.domain.Image;
import kz.am.imagehosting.domain.Post;
import kz.am.imagehosting.repository.ImageRepository;
import kz.am.imagehosting.repository.PostRepository;
import kz.am.imagehosting.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/images";

    @Autowired
    public PostService(PostRepository postRepository, ImageRepository imageRepository,
                       UserRepository userRepository) {
        this.postRepository = postRepository;
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
    }

    public Post findPostById(UUID id){
        return postRepository.findById(id).orElse(null);
    }
    public List<Post> findAllPosts(){
        return postRepository.findAll(Sort.by("createdAt").descending());
    }
    public void savePost(String postName, MultipartFile file) {
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

        post.setCreatedBy(userRepository.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
                ).orElse(null));

        postRepository.save(post);
    }
}
