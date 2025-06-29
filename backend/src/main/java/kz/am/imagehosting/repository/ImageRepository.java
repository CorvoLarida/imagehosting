package kz.am.imagehosting.repository;

import kz.am.imagehosting.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image, UUID> {
    Optional<Image> findImageByImageLocation(String imageLocation);
}
