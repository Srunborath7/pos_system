package com.example.pos_system.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    private String username;
    private String email;
    private String oldPassword;
    private String newPassword;
}
