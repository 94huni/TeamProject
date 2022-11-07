package com.springboot.Teamproject.DTO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Setter
@Getter
public class UserCreateForm {

    private String id;

    @Size(min = 8, max = 15)
    private String password1;

    @Size(min = 8, max = 15)
    private String password2;

    private String nickname;
}
