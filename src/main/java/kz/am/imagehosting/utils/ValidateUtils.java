package kz.am.imagehosting.utils;

import kz.am.imagehosting.dto.create.PostCollectionDto;
import kz.am.imagehosting.dto.create.PostDto;
import kz.am.imagehosting.dto.update.PostUpdateDto;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


public class ValidateUtils {
    public static void validatePostDto(PostDto postDto){
        String postName = postDto.getPostName();
        MultipartFile file = postDto.getPostImage();
        Integer accessId = postDto.getAccessId();
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
    public static void validatePostUpdateDto(PostUpdateDto postUpdateDto){
        String postName = postUpdateDto.getPostName();
        Integer accessId = postUpdateDto.getAccessId();
        if (!StringUtils.hasText(postName)) {
            throw new RuntimeException("Post must have a name");
        }
        if (accessId == null) {
            throw new RuntimeException("Post must have a access level");
        }
    }

    public static void validatePostCollectionDto(PostCollectionDto pcDto){
        String postCollectionName = pcDto.getPostCollectionName();
        UUID[] selectedPosts = pcDto.getSelectedPosts();
        if (!StringUtils.hasText(postCollectionName)) {
            throw new RuntimeException("Post collection must have a name");
        }
        if (selectedPosts == null) {
            throw new RuntimeException("Post collection must have selected posts object");
        }
    }
}
