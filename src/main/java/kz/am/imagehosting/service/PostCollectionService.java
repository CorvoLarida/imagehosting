package kz.am.imagehosting.service;

import kz.am.imagehosting.domain.Post;
import kz.am.imagehosting.domain.PostCollection;
import kz.am.imagehosting.repository.PostCollectionRepository;
import kz.am.imagehosting.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class PostCollectionService {
    private final PostService postService;
    private final PostCollectionRepository postCollectionRepository;

    @Autowired
    public PostCollectionService(PostService postService, PostCollectionRepository postCollectionRepository) {
        this.postService = postService;
        this.postCollectionRepository = postCollectionRepository;
    }
    public PostCollection getCollectionById(UUID id){
        return postCollectionRepository.findById(id).orElse(null);
    }
    public List<PostCollection> getAllCollections(){
        return postCollectionRepository.findAll();
    }
    public List<Post> getAllPosts(){
        return postService.findAllPosts();
    }
    public void createCollection(String postCollectionName, UUID[] selectedPosts){
        PostCollection postCollection = new PostCollection();
        postCollection.setPostCollectionName(postCollectionName);

        Set<Post> posts = new HashSet<>();
        Set<PostCollection> postCollections = new HashSet<>();
        for (UUID post: selectedPosts){
            posts.add(postService.findPostById(post));
        }
        postCollection.setCollectionPosts(posts);
        postCollections.add(postCollection);
        postCollectionRepository.save(postCollection);

        System.out.println("Method done");
    }
}
