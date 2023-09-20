package com.wirepick.wpksmsencryptionapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RSAPublicKey {
    private String publicKeyBase64String;
}