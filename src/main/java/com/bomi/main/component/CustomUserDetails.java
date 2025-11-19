package com.bomi.main.component;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;


@Getter
public class CustomUserDetails implements UserDetails {

    private final Long memberId;
    private final String email;
    private final String memberName;
    private final Collection<? extends GrantedAuthority> authorities; // 권한 정보

    public CustomUserDetails(Long memberId, String email, String memberName, Collection<? extends GrantedAuthority> authorities) {
        this.memberId = memberId;
        this.email = email;
        this.memberName = memberName;
        this.authorities = authorities;
    }


    // UserDetails 필수 구현 메서드
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 실제 프로젝트에서는 JWT claims에 담긴 Role 정보를 여기서 파싱하여 GrantedAuthority List를 생성해야 합니다.
        // 여기서는 예시로 빈 리스트를 반환합니다.
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return memberName;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}