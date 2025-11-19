package com.bomi.main.DTO;

import com.bomi.main.entity.Member;

public record MemberInfoDTO(String encryptedMemberId, String memberEmail, String memberName, String memberPhone) {
}
