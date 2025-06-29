package kz.am.imagehosting.repository;

import kz.am.imagehosting.domain.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<AuthUser, UUID> {
    Optional<AuthUser> findUserByUsername(String username);
    Optional<AuthUser> findFirstUserByUsername(String username);
}
