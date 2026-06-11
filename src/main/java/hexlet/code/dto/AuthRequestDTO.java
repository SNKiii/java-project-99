package hexlet.code.dto;

import lombok.Data;

@Data
public class AuthRequestDTO {
    private String username;  // email пользователя
    private String password;
}

