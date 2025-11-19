package com.bomi.main.DTO;

import com.bomi.main.entity.Member;

public record RequestUpdateMemberInfo(String newMemberName, String newMemberPhoneNumber) {
}
