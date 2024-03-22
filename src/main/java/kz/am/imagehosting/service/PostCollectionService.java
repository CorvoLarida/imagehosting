package kz.am.imagehosting.service;

import kz.am.imagehosting.domain.AuthUser;
import kz.am.imagehosting.domain.Post;
import kz.am.imagehosting.domain.PostCollection;
import kz.am.imagehosting.repository.PostCollectionRepository;
import kz.am.imagehosting.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PostCollectionService {
    private final PostService postService;
    private final PostCollectionRepository postCollectionRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostCollectionService(PostService postService, PostCollectionRepository postCollectionRepository, UserRepository userRepository) {
        this.postService = postService;
        this.postCollectionRepository = postCollectionRepository;
        this.userRepository = userRepository;
    }
    public PostCollection getCollectionById(UUID id){
        return postCollectionRepository.findById(id).orElse(null);
    }
    public List<PostCollection> getAllCollections(){
        return postCollectionRepository.findAll();
    }
    public List<PostCollection> getAllUserCollections(Authentication auth){
        AuthUser user = userRepository.findUserByUsername(auth.getName()).orElse(null);
        if (user != null) return postCollectionRepository.findPostCollectionByCreatedByOrderByPostCollectionNameAsc(user);
        return Collections.emptyList();
    }
    public List<Post> getAllPosts(){
        return postService.getAllPosts();
    }
    public void deleteCollection(PostCollection pc){
        postCollectionRepository.deleteById(pc.getId());
    }
    public void createCollection(String postCollectionName, UUID[] selectedPosts){

        Set<Post> posts = new HashSet<>();
        for (UUID post: selectedPosts){
            posts.add(postService.getPostById(post));
        }
        PostCollection postCollection = new PostCollection();
        postCollection.setPostCollectionName(postCollectionName);
        postCollection.setCollectionPosts(posts);
        postCollection.setCreatedBy(userRepository.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        ).orElse(null));
        postCollectionRepository.save(postCollection);
    }
}
