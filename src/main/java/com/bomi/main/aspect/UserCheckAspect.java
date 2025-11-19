package com.bomi.main.aspect;

import com.bomi.main.Exception.UserMemberNotMathException;
import com.bomi.main.component.CustomUserDetails;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UserCheckAspect {

    @Before("execution(public * com.bomi.main.service.MemberService.*(long, ..)) && args(memberId, ..)")
    public void checkUserAuthorization(JoinPoint joinPoint, long memberId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new UserMemberNotMathException(String.format("User is not a member of %s", memberId));
        }

        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        long userId = user.getMemberId();

        if (userId != memberId) {
            throw new UserMemberNotMathException(String.format("User is not a member of %s", memberId));
        }
    }

}
