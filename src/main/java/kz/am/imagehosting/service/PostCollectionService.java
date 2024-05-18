package kz.am.imagehosting.service;

import kz.am.imagehosting.domain.AuthUser;
import kz.am.imagehosting.domain.Post;
import kz.am.imagehosting.domain.PostCollection;
import kz.am.imagehosting.dto.create.PostCollectionDto;
import kz.am.imagehosting.repository.PostCollectionRepository;
import kz.am.imagehosting.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    private PostCollection setPosts(PostCollection pc, UUID[] selectedPosts){
        Set<Post> posts = new HashSet<>();
        if (selectedPosts != null) {
            for (UUID post: selectedPosts){
                posts.add(postService.getPostById(post));
            }
        }
        pc.setCollectionPosts(posts);
        return pc;
    }

    public PostCollection getCollectionById(UUID id){
        return postCollectionRepository.findById(id).orElse(null);
    }

    public List<PostCollection> getAllCollections(){
        return postCollectionRepository.findAll();
    }

    public List<PostCollection> getAllCollections(String username){
        AuthUser user = userRepository.findUserByUsername(username).orElse(null);
        if (user != null) return postCollectionRepository.findPostCollectionsByCreatedByOrderByPostCollectionNameAsc(user);
        return Collections.emptyList();
    }

    public List<Post> getAllPosts(){
        return postService.getAllPosts();
    }
    public List<Post> getAllPosts(Authentication auth){
        return postService.getAllPosts(auth);
    }
    public List<Post> getCollectionPosts(Authentication auth, PostCollection pc) {
        Set<Post> pcPosts = pc.getCollectionPosts();
        if (pcPosts != null) return pcPosts.stream().filter(post -> postService.canUserSeePost(post, auth)).collect(Collectors.toList());
        return Collections.emptyList();
    }

    public void createCollection(PostCollectionDto postCollectionDto){
        PostCollection postCollection = new PostCollection();
        postCollection.setPostCollectionName(postCollectionDto.getPostCollectionName());
        this.setPosts(postCollection, postCollectionDto.getSelectedPosts());
        postCollection.setCreatedBy(userRepository.findUserByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()
        ).orElse(null));
        postCollectionRepository.save(postCollection);
    }

    public void updateCollection(PostCollection pc, String postCollectionName, UUID[] selectedPosts){
        this.setPosts(pc, selectedPosts);
        pc.setPostCollectionName(postCollectionName);
        postCollectionRepository.save(pc);
    }

    public void deleteCollection(PostCollection pc){
        postCollectionRepository.deleteById(pc.getId());
    }

}
