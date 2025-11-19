package com.bomi.main.entity;

import com.bomi.main.domain.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Member extends Global {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long memberId;

    @Column(nullable = false)
    private String memberEmail;

    @Column(nullable = false)
    private String memberDisplayName;

    private String memberPhoneNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    // 피보호자
    @OneToMany(mappedBy = "ward")
    private List<Care> wards = new ArrayList<>();

    // 보호자
    @OneToMany(mappedBy = "guardian")
    private List<Care> guardians = new ArrayList<>();

    public void updateMemberInfo(String newMemberName, String newMemberPhoneNumber) {
        if (!newMemberName.equals(this.memberDisplayName)) {
            this.memberDisplayName = newMemberName;
        }

        if (!newMemberPhoneNumber.equals(this.memberPhoneNumber)) {
            this.memberPhoneNumber = newMemberPhoneNumber;
        }
    }

//    @Column(nullable = false)
//    @Enumerated(EnumType.STRING)
//    private SocialType socialType;
}
