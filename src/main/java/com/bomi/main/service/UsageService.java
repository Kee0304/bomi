package com.bomi.main.service;

import com.bomi.main.DTO.DailyUsageDTO;
import com.bomi.main.DTO.DailyUsageListDTO;
import com.bomi.main.entity.DailyUsage;
import com.bomi.main.entity.DailyUsageId;
import com.bomi.main.repository.UsageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsageService {

    private final int PAGE_SIZE = 7;
    private final UsageRepository usageRepository;

    public DailyUsageListDTO getUsageData(Long memberId, String localDate) {
        LocalDate endDate = LocalDate.parse(localDate).plusDays(1);
        LocalDate startDate = endDate.minusDays(PAGE_SIZE);

        List<DailyUsage> usageList = usageRepository
                .findByDailyUsageId_MemberIdAndDailyUsageId_DateBetweenOrderByDailyUsageId_DateDesc(
                        memberId,
                        startDate,
                        endDate
                );

        List<DailyUsageDTO> usageDTOList = usageList.stream()
                .map(DailyUsageDTO::new)
                .toList();

        return new DailyUsageListDTO(usageDTOList);
    }

    @Transactional
    public void postDailyUsage(Long memberId, DailyUsageDTO dailyUsageDTO) {
        Optional<DailyUsage> dailyUsage = usageRepository.findByDailyUsageId_MemberIdAndDailyUsageId_Date(memberId, LocalDate.now());
        if (dailyUsage.isPresent()) {
            DailyUsage usage = dailyUsage.get();
            usage.updateUsageMinute(dailyUsageDTO.usageMinute());
        } else {
            DailyUsageId dailyUsageId = DailyUsageId.builder()
                    .memberId(memberId)
                    .date(LocalDate.now())
                    .build();
            DailyUsage usage = DailyUsage.builder()
                    .dailyUsageId(dailyUsageId)
                    .usageMinute(dailyUsageDTO.usageMinute())
                    .build();
            usageRepository.save(usage);
        }
    }
}
