package com.bomi.main.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyUsage extends Global {
    @EmbeddedId
    private DailyUsageId dailyUsageId;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer usageMinute;

    public void updateUsageMinute(Integer newMinute) {
        this.usageMinute = newMinute;
    }
}
