package kz.am.imagehosting.utils;

import kz.am.imagehosting.domain.Post;
import org.springframework.security.core.Authentication;

public class UserUtils {

    public static boolean canDelete(Authentication auth, String username){
        boolean canDelete = false;
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
        if (auth.getName().equals(username) || isAdmin) canDelete = true;
        return canDelete;
    }
    public static boolean canUserAccessPost(Authentication auth, Post post){
        boolean canUserAccessPost = false;
        if (post.getAccess().getName().equals("PUBLIC")) canUserAccessPost = true;
        else {
            boolean isAdmin = auth.getAuthorities().stream()
                    .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
            if (auth.getName().equals(post.getCreatedBy().getUsername()) || isAdmin) canUserAccessPost = true;
        }
        return canUserAccessPost;
    }
    public static boolean canSeeAllUserPosts(Authentication auth, String username){
        return auth.getName().equals(username) || auth.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
    }
}
