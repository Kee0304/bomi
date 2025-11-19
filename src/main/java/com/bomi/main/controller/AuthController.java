package com.bomi.main.controller;


import com.bomi.main.DTO.LoginRequestDTO;
import com.bomi.main.DTO.LoginResponseDTO;
import com.bomi.main.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Value("${spring.security.jwt.refresh-expiration}")
    private long refreshExpiration;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> signIn(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response) throws Exception {
        String[] tokens = authService.signIn(loginRequestDTO);
        Cookie refresthTokenCookie = createRefreshTokenCookie(tokens[1]);
        response.addCookie(refresthTokenCookie);
        return new ResponseEntity<>(new LoginResponseDTO(tokens[0]), HttpStatus.OK);
    }

    private Cookie createRefreshTokenCookie(String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) refreshExpiration);

        return cookie;
    }

}
