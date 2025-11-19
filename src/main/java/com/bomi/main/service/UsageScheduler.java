package com.bomi.main.service;

import com.bomi.main.entity.DailyUsage;
import com.bomi.main.entity.DailyUsageId;
import com.bomi.main.repository.MemberRepository;
import com.bomi.main.repository.UsageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsageScheduler {

    private final MemberRepository memberRepository;
    private final UsageRepository usageRepository;

    @Scheduled(cron = "0 58 23 * * *")
    @Transactional
    public void generateZeroUsage() {
        Long count = 0L;

        LocalDate today = LocalDate.now();

        Set<Long> allMemberIds = memberRepository.findAllMemberIds();

        List<DailyUsage> existingUsages = usageRepository.findAllById(
                allMemberIds.stream()
                        .map(memberId -> DailyUsageId.builder().memberId(memberId).date(today).build())
                        .collect(Collectors.toList())
        );

        Set<Long> existingMemberIds = existingUsages.stream()
                .map(usage -> usage.getDailyUsageId().getMemberId())
                .collect(Collectors.toSet());

        List<Long> missingMemberIds = allMemberIds.stream()
                .filter(memberId -> !existingMemberIds.contains(memberId))
                .toList();

        for (Long memberId : missingMemberIds) {
            DailyUsageId dailyUsageId = DailyUsageId.builder().memberId(memberId).build();
            DailyUsage dailyUsage = DailyUsage.builder()
                    .dailyUsageId(dailyUsageId)
                    .usageMinute(0)
                    .build();
            usageRepository.save(dailyUsage);
            count++;
        }
    }


}
