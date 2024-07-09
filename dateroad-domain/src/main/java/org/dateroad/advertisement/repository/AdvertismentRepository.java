package org.dateroad.advertisement.repository;

import java.util.List;
import org.dateroad.advertisement.domain.Advertisment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvertismentRepository extends JpaRepository<Advertisment, Long> {
    @Query("SELECT a FROM Advertisment a ORDER BY a.createdAt DESC")
    List<Advertisment> findTop5ByOrderByCreatedDateDesc(Pageable pageable);
}
