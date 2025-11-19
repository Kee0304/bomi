package com.bomi.main.DTO;

import com.bomi.main.entity.Member;

public record MemberDetailDTO(String memberEmail, String memberDisplayName) {

    public MemberDetailDTO(Member member) {
        this(
                member.getMemberEmail(),
                member.getMemberDisplayName()
        );
    }
}
