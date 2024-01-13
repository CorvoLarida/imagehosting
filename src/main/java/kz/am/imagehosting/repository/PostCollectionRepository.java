package kz.am.imagehosting.repository;

import kz.am.imagehosting.domain.Post;
import kz.am.imagehosting.domain.PostCollection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostCollectionRepository extends JpaRepository<PostCollection, UUID> {

}
