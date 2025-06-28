package com.tikitaka.api.dto.verify;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuth2VerificationData {
    private String code;
    private String email;
    private String sub;
    private String name;
}