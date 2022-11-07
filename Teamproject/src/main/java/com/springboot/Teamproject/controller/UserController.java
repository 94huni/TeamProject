package com.springboot.Teamproject.controller;

import com.springboot.Teamproject.DTO.UserCreateForm;
import com.springboot.Teamproject.entity.User;
import com.springboot.Teamproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm){

        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult){

        if(bindingResult.hasErrors())
            return "signup_form";

        if(!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())){

            bindingResult.rejectValue("password2","passwordInCorrect","2개의 패스워드가 일치하지 않습니다");
            System.out.println("2개의 패스워드가 일치하지 않습니다.");

            return "signup_form";
        }

        try{
            userService.create(userCreateForm.getId(),userCreateForm.getPassword1(),userCreateForm.getNickname());

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

    @GetMapping("/login")
    public String login(){
        return "login_form";
    }

}