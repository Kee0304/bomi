package com.bomi.main.repository;

import com.bomi.main.entity.Care;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface CareRepository extends JpaRepository<Care, Long> {

    @Query("SELECT c FROM Care c JOIN FETCH c.ward w WHERE c.guardian.memberId = :guardianId")
    Set<Care> findWardsByGuardianId(@Param("guardianId") Long guardianId);

    @Query("SELECT c FROM Care c JOIN FETCH c.guardian g WHERE c.ward.memberId = :wardId")
    Set<Care> findGuardiansByWardId(@Param("wardId") Long wardId);

    @Query("SELECT c FROM Care c WHERE c.ward.memberId = :wardId AND c.guardian.memberId = :guardianId")
    Optional<Care> findByWardIdAndGuardianId(@Param("wardId") Long wardId, @Param("guardianId") Long guardianId);
}
