package com.nttdata.test.dto.request;

import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDto {
    private String name;
    private String email;
    private String password;
    private List<PhoneDto> phones;
}
