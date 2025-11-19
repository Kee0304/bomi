package com.bomi.main.Filter;

import com.bomi.main.component.CustomUserDetails;
import com.bomi.main.component.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JWTFilter implements Filter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String path = request.getRequestURI();
        if (path.startsWith("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = jwtTokenProvider.extractAccessToken(request);

        Claims claims = jwtTokenProvider.validateToken(accessToken);

        System.out.println(String.format("accessToken = %s", accessToken));
        System.out.println(claims);

        String encryptedMemberId = claims.getSubject();
        Long memberId = Long.parseLong(jwtTokenProvider.decryptId(encryptedMemberId));

        String email = (String) claims.get("email");
        String memberName = (String) claims.get("memberName");
        String role = claims.get("role", String.class);

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

        CustomUserDetails customUserDetails = new CustomUserDetails(
                memberId,
                email,
                memberName,
                authorities
        );

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                customUserDetails,
                null,
                customUserDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
