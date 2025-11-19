package com.bomi.main.controller;

import com.bomi.main.DTO.RequestPostWardLocation;
import com.bomi.main.DTO.WardsLocationDTO;
import com.bomi.main.domain.DecodedMemberId;
import com.bomi.main.service.MemberLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/location")
@RequiredArgsConstructor
public class MemberLocationController {

    private final MemberLocationService memberLocationService;

    @GetMapping("/wards/{guardianId}")
    public ResponseEntity<WardsLocationDTO> getWardsLocation(@PathVariable("guardianId") String encryptedGuardianId, @DecodedMemberId("guardianId") Long guardianId) {
        return new ResponseEntity<>(memberLocationService.getWardsLocation(guardianId), HttpStatus.OK);
    }

    @PostMapping("/{wardId}")
    public ResponseEntity<?> postWardLocation(
            @PathVariable("wardId") String encryptedWardId,
            @DecodedMemberId("wardId") Long wardId,
            @RequestBody RequestPostWardLocation requestPostWardLocation
            ) {
        memberLocationService.postWardLocation(wardId, requestPostWardLocation);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
