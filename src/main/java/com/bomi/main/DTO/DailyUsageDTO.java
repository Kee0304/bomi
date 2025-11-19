package com.bomi.main.DTO;

import com.bomi.main.entity.DailyUsage;

import java.time.LocalDate;

public record DailyUsageDTO(LocalDate date, Integer usageMinute) {

    public DailyUsageDTO(DailyUsage usage) {
        this(
                usage.getDailyUsageId().getDate(),
                usage.getUsageMinute()
        );
    }
}
