package com.bomi.main.service;

import com.bomi.main.DTO.LoginRequestDTO;
import com.bomi.main.component.JwtTokenProvider;
import com.bomi.main.domain.Role;
import com.bomi.main.domain.SocialType;
import com.bomi.main.entity.Member;
import com.bomi.main.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;

    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public Member signUp(String email, String memberDisplayName, String memberPhoneNumber) {
        Member member = Member.builder()
                .memberEmail(email)
                .memberDisplayName(memberDisplayName)
                .memberPhoneNumber(memberPhoneNumber)
                .role(Role.USER)
                .build();

        return memberRepository.save(member);
    }


    public String[] signIn(LoginRequestDTO loginRequestDTO) {
        String email = loginRequestDTO.email();
        Member member = memberRepository.findByMemberEmail(email).orElseGet(() -> signUp(loginRequestDTO.email(), loginRequestDTO.memberName(), loginRequestDTO.memberPhone()));

        String accessToken = jwtTokenProvider.generateAccessToken(String.valueOf(member.getMemberId()), email, member.getMemberDisplayName(), String.valueOf(member.getRole()));
        String refreshToken = jwtTokenProvider.generateRefreshToken(String.valueOf(member.getMemberId()));

        return new String[]{accessToken, refreshToken};
    }

}
