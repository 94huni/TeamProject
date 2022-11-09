package com.springboot.Teamproject.DTO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Setter
@Getter
public class UserCreateForm {

    private String id;      //아이디

    @Size(min = 8, max = 15)
    private String password1;   //비밀번호

    @Size(min = 8, max = 15)
    private String password2;   //비밀번호 확인

    private String nickname;    //닉네임
}
