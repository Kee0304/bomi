package com.bomi.main.DTO;

public record LoginRequestDTO(String email, String memberName, String memberPhone) {
    public LoginRequestDTO {
        if (memberPhone == null) {
            memberPhone = "";
        }
    }
}
