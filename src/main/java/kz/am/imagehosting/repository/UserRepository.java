package kz.am.imagehosting.repository;

import kz.am.imagehosting.domain.Image;
import kz.am.imagehosting.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findUserByUsername(String username);
}
