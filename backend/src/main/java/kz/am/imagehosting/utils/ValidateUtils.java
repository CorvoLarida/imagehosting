package kz.am.imagehosting.utils;

import kz.am.imagehosting.dto.create.PostCollectionDTO;
import kz.am.imagehosting.dto.create.PostDTO;
import kz.am.imagehosting.dto.update.PostUpdateDTO;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


public class ValidateUtils {
    public static void validatePostDTO(PostDTO postDTO){
        String postName = postDTO.getPostName();
        MultipartFile file = postDTO.getPostImage();
        Integer accessId = postDTO.getAccessId();
        if (!StringUtils.hasText(postName)) {
            throw new RuntimeException("Post must have a name");
        }
        if (file == null) {
            throw new RuntimeException("Post must have an image");
        }
        if (accessId == null) {
            throw new RuntimeException("Post must have a access level");
        }
    }
    public static void validatePostUpdateDTO(PostUpdateDTO postUpdateDTO){
        String postName = postUpdateDTO.getPostName();
        Integer accessId = postUpdateDTO.getAccessId();
        if (!StringUtils.hasText(postName)) {
            throw new RuntimeException("Post must have a name");
        }
        if (accessId == null) {
            throw new RuntimeException("Post must have a access level");
        }
    }

    public static void validatePostCollectionDTO(PostCollectionDTO pcDTO){
        String postCollectionName = pcDTO.getPostCollectionName();
        UUID[] selectedPosts = pcDTO.getSelectedPosts();
        if (!StringUtils.hasText(postCollectionName)) {
            throw new RuntimeException("Post collection must have a name");
        }
        if (selectedPosts == null) {
            throw new RuntimeException("Post collection must have selected posts object");
        }
    }
}
