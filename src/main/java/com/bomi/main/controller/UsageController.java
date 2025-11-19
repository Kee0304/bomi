package com.bomi.main.controller;

import com.bomi.main.DTO.DailyUsageDTO;
import com.bomi.main.DTO.DailyUsageListDTO;
import com.bomi.main.domain.DecodedMemberId;
import com.bomi.main.service.UsageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usage")
@RequiredArgsConstructor
public class UsageController {

    private final UsageService usageService;

    @GetMapping("/{memberId}")
    public ResponseEntity<DailyUsageListDTO> getUsageData(
            @PathVariable("memberId") String encryptedMemberId,
            @DecodedMemberId("memberId") Long memberId,
            @RequestParam String localDate
    ) {
        return new ResponseEntity<>(usageService.getUsageData(memberId, localDate), HttpStatus.OK);
    }

    @PostMapping("/{memberId}")
    public ResponseEntity<?> postUsageData(
            @PathVariable("memberId") String encryptedMemberId,
            @DecodedMemberId("memberId") Long memberId,
            @RequestBody DailyUsageDTO dailyUsageDTO
    ) {
        usageService.postDailyUsage(memberId, dailyUsageDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
