package com.bomi.main.repository;

import com.bomi.main.entity.MemberLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface MemberLocationRepository extends JpaRepository<MemberLocation, Long> {
    @Query("""
        SELECT ml
        FROM MemberLocation ml
        WHERE ml.id IN (
            SELECT MAX(m2.id)
            FROM MemberLocation m2
            WHERE m2.memberId IN :memberIds
            GROUP BY m2.memberId
        )
    """)
    List<MemberLocation> findLatestLocationsByMemberIds(Collection<Long> memberIds);
}
