package com.bomi.main.component;

import com.bomi.main.domain.DecodedMemberId;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class DecodeMemberIdArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAnnotation = parameter.hasParameterAnnotation(DecodedMemberId.class);
        boolean isLong = (parameter.getParameterType() == long.class ||
                parameter.getParameterType() == Long.class);
        return hasAnnotation && isLong;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        DecodedMemberId decodedMemberIdAnnotation = parameter.getParameterAnnotation(DecodedMemberId.class);

        @SuppressWarnings("unchecked")
        Map<String, String> pathVariables = (Map<String, String>) webRequest.getAttribute(
                HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, NativeWebRequest.SCOPE_REQUEST
        );


        String pathVariableName = decodedMemberIdAnnotation.value();
        if (pathVariableName.isEmpty()) {
            pathVariableName = parameter.getParameterName();
        }

        String encryptedMemberId = pathVariables.get(pathVariableName);

        try {
            return Long.parseLong(jwtTokenProvider.decryptId(encryptedMemberId));
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Failed to decode memberId: " + encryptedMemberId, e
            );
        }
    }
}



