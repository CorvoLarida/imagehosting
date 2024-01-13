package kz.am.imagehosting.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "post_collections")
public class PostCollection {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotNull
    private String postCollectionName;

    @ManyToMany
    @JoinTable(
            name = "post_collections_posts",
            joinColumns = @JoinColumn(name = "post_collection_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id"))
    private Set<Post> collectionPosts;

    public PostCollection(){    }
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPostCollectionName() {
        return postCollectionName;
    }

    public void setPostCollectionName(String postCollectionName) {
        this.postCollectionName = postCollectionName;
    }

    public Set<Post> getCollectionPosts() {
        return collectionPosts;
    }

    public void setCollectionPosts(Set<Post> collectionPosts) {
        this.collectionPosts = collectionPosts;
    }
}
