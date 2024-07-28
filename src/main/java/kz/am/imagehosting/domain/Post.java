package kz.am.imagehosting.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotNull
    private String postName;
    @NotNull
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "access_id", referencedColumnName = "id")
    private PostAccess access;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "created_by_id", referencedColumnName = "id")
    private AuthUser createdBy;
    private ZonedDateTime createdAt = ZonedDateTime.now();
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "post_collections_posts",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "post_collection_id"))
    private Set<PostCollection> postCollections;
    public Post(){}
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public PostAccess getAccess() {
        return access;
    }

    public void setAccess(PostAccess access) {
        this.access = access;
    }

    public String getPhotosImagePath() {
        if (this.image == null || this.id == null) return null;
        return this.image.getImageLocation();
    }

    public Set<PostCollection> getPostCollections() {
        return postCollections;
    }

    public void setPostCollections(Set<PostCollection> postCollections) {
        this.postCollections = postCollections;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public AuthUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(AuthUser createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", postName='" + postName + '\'' +
                ", image=" + image +
                ", postCollections=" + postCollections +
                '}';
    }
}
