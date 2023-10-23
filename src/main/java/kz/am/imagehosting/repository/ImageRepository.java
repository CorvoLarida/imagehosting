package kz.am.imagehosting.repository;

import kz.am.imagehosting.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
