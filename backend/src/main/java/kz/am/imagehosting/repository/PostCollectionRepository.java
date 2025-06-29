package kz.am.imagehosting.repository;

import kz.am.imagehosting.domain.AuthUser;
import kz.am.imagehosting.domain.PostCollection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PostCollectionRepository extends JpaRepository<PostCollection, UUID> {
    List<PostCollection> findPostCollectionsByCreatedByOrderByPostCollectionNameAsc(AuthUser createdBy);

}
