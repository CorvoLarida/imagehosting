package kz.am.imagehosting.repository;

import kz.am.imagehosting.domain.AuthRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<AuthRole, Integer> {
    Optional<AuthRole> findRoleByName(String name);
}
