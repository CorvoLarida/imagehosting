package kz.am.imagehosting.repository;

import kz.am.imagehosting.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
