package kz.am.imagehosting.repository;

import kz.am.imagehosting.domain.PostAccess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccessRepository extends JpaRepository<PostAccess, Integer> {
    Optional<PostAccess> findByName(String name);
    List<PostAccess> findAllByName(String name);
}
