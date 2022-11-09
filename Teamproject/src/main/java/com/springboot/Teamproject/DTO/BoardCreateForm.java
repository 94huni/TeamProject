package com.springboot.Teamproject.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class BoardCreateForm {

    private String title;       //제목

    private String content;     //내용

    private MultipartFile file;    //이미지 파일
}
