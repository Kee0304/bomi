package com.bomi.main.service;

import com.bomi.main.DTO.*;
import com.bomi.main.component.CustomUserDetails;
import com.bomi.main.component.JwtTokenProvider;
import com.bomi.main.entity.Care;
import com.bomi.main.entity.Member;
import com.bomi.main.repository.CareRepository;
import com.bomi.main.repository.MemberRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final CareRepository careRepository;

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberInfoDTO getMemberInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        Long userId = user.getMemberId();
        return getMemberInfo(userId);
    }

    public MemberInfoDTO getMemberInfo(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
        String encryptedMemberId = jwtTokenProvider.encryptId(String.valueOf(memberId));
        return new MemberInfoDTO(encryptedMemberId, member.getMemberEmail(), member.getMemberDisplayName(), member.getMemberPhoneNumber());
    }

    @Transactional
    public MemberInfoDTO updateMemberInfo(Long memberId, RequestUpdateMemberInfo requestUpdateMemberInfo) {
        Member member = memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
        member.updateMemberInfo(requestUpdateMemberInfo.newMemberName(), requestUpdateMemberInfo.newMemberPhoneNumber());
        return getMemberInfo(memberId);
    }

    public WardsDTO getMemberWards(Long memberId) {
        Set<Care> wards = careRepository.findWardsByGuardianId(memberId);
        List<WardInfoDTO> wardMembers = wards.stream()
                .map(Care::getWard)
                .map(member -> {
                    String encryptedMemberId = jwtTokenProvider.encryptId(String.valueOf(member.getMemberId()));
                    return new WardInfoDTO(encryptedMemberId, member.getMemberEmail(), member.getMemberDisplayName(), member.getMemberPhoneNumber());
                })
                .toList();
        return new WardsDTO(wardMembers);
    }

    public GuardiansDTO getMemberGuardians(Long memberId) {
        Set<Care> guardians = careRepository.findGuardiansByWardId(memberId);
        List<GuardianInfoDTO> guardianMembers = guardians.stream()
                .map(Care::getGuardian)
                .map(member -> {
                    String encryptedMemberId = jwtTokenProvider.encryptId(String.valueOf(member.getMemberId()));
                    return new GuardianInfoDTO(encryptedMemberId, member.getMemberEmail(), member.getMemberDisplayName(), member.getMemberPhoneNumber());
                })
                .toList();
        return new GuardiansDTO(guardianMembers);
    }


    @Transactional
    public GuardiansDTO postGuardian(Long memberId, @RequestBody RequestPostGuardianDTO requestPostGuardianDTO) {
        Member member = memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
        Member guardian = memberRepository.findByMemberEmail(requestPostGuardianDTO.email())
                .orElseGet(() -> authService.signUp(requestPostGuardianDTO.email(), requestPostGuardianDTO.memberName(), requestPostGuardianDTO.memberPhone()));

        if (guardian.getMemberPhoneNumber() == null || guardian.getMemberPhoneNumber().isEmpty()) {
            RequestUpdateMemberInfo requestUpdateMemberInfo = new RequestUpdateMemberInfo(requestPostGuardianDTO.memberName(), requestPostGuardianDTO.memberPhone());
            updateMemberInfo(guardian.getMemberId(), requestUpdateMemberInfo);
        }

        careRepository.findByWardIdAndGuardianId(member.getMemberId(), guardian.getMemberId())
                .ifPresent(c -> { throw new EntityExistsException("이미 존재하는 관계"); });

        Care newCare = Care.builder()
                .guardian(guardian)
                .ward(member)
                .build();
        careRepository.save(newCare);

        return getMemberGuardians(memberId);
    }

    @Transactional
    public GuardiansDTO deleteGuardian(Long memberId, String encryptedGuardianId) {
        Long guardianId = Long.parseLong(jwtTokenProvider.decryptId(encryptedGuardianId));
        Member member = memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
        Member guardian = memberRepository.findById(guardianId).orElseThrow(EntityNotFoundException::new);
        Care care = careRepository.findByWardIdAndGuardianId(member.getMemberId(), guardian.getMemberId()).orElse(null);

        if (care == null) {
            return getMemberGuardians(memberId);
        }

        Set<Care> guardians = careRepository.findGuardiansByWardId(memberId);
        guardians.remove(care);
        care.softDelete();

        return getMemberGuardians(memberId);
    }
}
