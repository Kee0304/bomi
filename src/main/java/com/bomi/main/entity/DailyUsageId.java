package com.bomi.main.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@EqualsAndHashCode
@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyUsageId implements Serializable {
    @Column(name="member_id")
    Long memberId;

    @Column(name="date")
    LocalDate date;
}
