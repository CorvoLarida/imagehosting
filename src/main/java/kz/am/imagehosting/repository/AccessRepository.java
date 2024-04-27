package kz.am.imagehosting.repository;

import kz.am.imagehosting.domain.PostAccess;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessRepository extends JpaRepository<PostAccess, Integer> {
}
