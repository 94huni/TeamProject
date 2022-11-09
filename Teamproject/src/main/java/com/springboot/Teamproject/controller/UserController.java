package com.springboot.Teamproject.controller;

import com.springboot.Teamproject.DTO.UserCreateForm;
import com.springboot.Teamproject.entity.User;
import com.springboot.Teamproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    //회원가입 홈페이지로 이동
    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm){

        return "signup_form";
    }

    //회원가입 시 정보를 등록
    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult){

        if(bindingResult.hasErrors())
            return "signup_form";

        if(!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())){       //패스워드와 패스워드 확인의 정보를 비교

            bindingResult.rejectValue("password2","passwordInCorrect","2개의 패스워드가 일치하지 않습니다");
            System.out.println("2개의 패스워드가 일치하지 않습니다.");

            return "signup_form";
        }

        try{
            userService.create(userCreateForm.getId(),userCreateForm.getPassword1(),userCreateForm.getNickname());  //등록

        }catch (DataIntegrityViolationException e){
            e.printStackTrace();
            bindingResult.reject("signupFailed","이미 등록된 사용자입니다.");
            System.out.println("이미 등록된 사용자입니다");
        }catch(Exception e){
            e.printStackTrace();
            bindingResult.reject("signupFailed",e.getMessage());
            System.out.println(e.getMessage());
            return "signup_form";
        }

        return "redirect:/";
    }

    //로그인 홈페이지로 이동
    @GetMapping("/login")
    public String login(){
        return "login_form";
    }

    //회원 탈퇴 기능
    @GetMapping("/withdrawal")
    public String withdrawal(Principal principal){

        User user = this.userService.getUser(principal.getName());  //현재 접속한 유저 정보를 가져옴

        this.userService.delete(user);

        return "redirect:/user/logout"; //회원 탈퇴 후 로그아웃
    }

}
