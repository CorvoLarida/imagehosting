package kz.am.imagehosting.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

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
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;

    @ManyToMany(mappedBy = "collectionPosts")
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
