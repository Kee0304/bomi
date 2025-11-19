package com.bomi.main.controller;

import com.bomi.main.DTO.GuardiansDTO;
import com.bomi.main.DTO.MemberInfoDTO;
import com.bomi.main.DTO.RequestPostGuardianDTO;
import com.bomi.main.DTO.WardsDTO;
import com.bomi.main.domain.DecodedMemberId;
import com.bomi.main.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberInfoDTO> getMemberInfo(
            @PathVariable("memberId") String encryptedMemberId, @DecodedMemberId("memberId") Long memberId) {
        return new ResponseEntity<>(memberService.getMemberInfo(memberId), HttpStatus.OK);
    }

    @GetMapping("/ward/{memberId}")
    public ResponseEntity<WardsDTO> getMemberWards(
            @PathVariable("memberId") String encryptedMemberId, @DecodedMemberId("memberId") Long memberId) {
        return new ResponseEntity<>(memberService.getMemberWards(memberId), HttpStatus.OK);
    }

    @GetMapping("/guardian/{memberId}")
    public ResponseEntity<GuardiansDTO> getMemberGuardians(
            @PathVariable("memberId") String encryptedMemberId, @DecodedMemberId("memberId") Long memberId) {
        return new ResponseEntity<>(memberService.getMemberGuardians(memberId), HttpStatus.OK);
    }

    @PostMapping("/guardian/{memberId}")
    public ResponseEntity<GuardiansDTO> postGuardian(
            @PathVariable("memberId") String encryptedMemberId, @DecodedMemberId("memberId") Long memberId, @RequestBody RequestPostGuardianDTO requestPostGuardianDTO) {
        return new ResponseEntity<>(memberService.postGuardian(memberId ,requestPostGuardianDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/guardian/{memberId}/delete/{guardianId}")
    public ResponseEntity<GuardiansDTO> deleteGuardian(
            @PathVariable("memberId") String encryptedMemberId, @DecodedMemberId("memberId") Long memberId, @PathVariable("guardianId") String encryptedGuardianId) {
        return new ResponseEntity<>(memberService.deleteGuardian(memberId,encryptedGuardianId), HttpStatus.OK);
    }
}
