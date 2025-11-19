package com.bomi.main.repository;

import com.bomi.main.entity.DailyUsage;
import com.bomi.main.entity.DailyUsageId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsageRepository extends JpaRepository<DailyUsage, DailyUsageId> {

    List<DailyUsage> findByDailyUsageId_MemberIdAndDailyUsageId_DateBetweenOrderByDailyUsageId_DateDesc(
            Long memberId,
            LocalDate startDate, // 포함 (Inclusive)
            LocalDate endDate // 미포함 (Exclusive)
    );

    Optional<DailyUsage> findByDailyUsageId_MemberIdAndDailyUsageId_Date(Long memberId, LocalDate today);

}
