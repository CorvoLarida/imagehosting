package kz.am.imagehosting.dto;

import java.util.UUID;

public class PostCollectionDto {
    private String postCollectionName;
    private UUID[] selectedPosts;

    public String getPostCollectionName() {
        return postCollectionName;
    }

    public void setPostCollectionName(String postCollectionName) {
        this.postCollectionName = postCollectionName;
    }

    public UUID[] getSelectedPosts() {
        return selectedPosts;
    }

    public void setSelectedPosts(UUID[] selectedPosts) {
        this.selectedPosts = selectedPosts;
    }
}
