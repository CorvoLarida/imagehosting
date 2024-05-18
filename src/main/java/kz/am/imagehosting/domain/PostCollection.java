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

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "post_collections_posts",
            joinColumns = @JoinColumn(name = "post_collection_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id"))
    @OrderBy("createdAt DESC")
    private Set<Post> collectionPosts;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "created_by_id", referencedColumnName = "id")
    private AuthUser createdBy;

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

    public AuthUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(AuthUser createdBy) {
        this.createdBy = createdBy;
    }
}
