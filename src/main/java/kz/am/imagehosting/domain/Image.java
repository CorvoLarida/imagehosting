package kz.am.imagehosting.domain;

import jakarta.persistence.*;
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Lob
    @Column(name = "image_location")
    private String imageLocation;

    public Image() {
    }

    public Image(String imageLocation) {
        this.imageLocation = imageLocation;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }
}


