package org.dateroad.adImage.repository;

import java.util.List;
import org.dateroad.adImage.domain.AdImage;
import org.dateroad.advertisement.domain.Advertisment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdImageRepository extends JpaRepository<AdImage, Long> {
    List<AdImage> findAllById(Long advertismentId);
}
