package kz.am.imagehosting.dto;

import kz.am.imagehosting.domain.PostAccess;
import org.springframework.web.multipart.MultipartFile;

public class PostDto {
    private String postName;
    private MultipartFile postImage;
    private Integer accessId;

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public MultipartFile getPostImage() {
        return postImage;
    }

    public void setPostImage(MultipartFile postImage) {
        this.postImage = postImage;
    }

    public Integer getAccessId() {
        return accessId;
    }

    public void setAccess(Integer accessId) {
        this.accessId = accessId;
    }
}
