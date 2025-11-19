package com.bomi.main.DTO;

import java.time.LocalDateTime;

public record WardLocationDetail(
        Long wardId,
        String wardDisplayName,
        Double latitude,
        Double longitude,
        LocalDateTime timestamp
) {}
