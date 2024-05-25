package kz.am.imagehosting.utils;

import kz.am.imagehosting.domain.Post;
import org.springframework.security.core.Authentication;

public class UserUtils {

    public static boolean canSeeAllUserPosts(Authentication auth, String username){
        if (auth == null) return false;
        return auth.getName().equals(username) || auth.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
    }
    public static boolean canUserAccessPost(Authentication auth, Post post){
        if (post.getAccess().getType().equals("PUBLIC")) return true;
        if (auth == null) return false;
        else {
            boolean isAdmin = auth.getAuthorities().stream()
                    .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
            return auth.getName().equals(post.getCreatedBy().getUsername()) || isAdmin;
        }
    }

}
