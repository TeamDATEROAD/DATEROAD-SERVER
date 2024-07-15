package org.dateroad.advertisement.repository;

import java.util.List;

import org.dateroad.advertisement.domain.Advertisement;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    @Query("SELECT a FROM Advertisement a ORDER BY a.createdAt DESC")
    List<Advertisement> findTop5ByOrderByCreatedDateDesc(Pageable pageable);
}
