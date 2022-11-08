package com.springboot.Teamproject.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class BoardCreateForm {

    private String title;

    private String content;

    private MultipartFile file;
}
