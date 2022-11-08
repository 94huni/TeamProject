package com.springboot.Teamproject.controller;

import com.springboot.Teamproject.DTO.CommentCreateForm;
import com.springboot.Teamproject.entity.BlogBoard;
import com.springboot.Teamproject.entity.Comment;
import com.springboot.Teamproject.entity.User;
import com.springboot.Teamproject.service.BlogBoardService;
import com.springboot.Teamproject.service.CommentService;
import com.springboot.Teamproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String commentCreate(@Valid CommentCreateForm commentCreateForm, Principal principal){

        User user = this.userService.getUser(principal.getName());

        this.commentService.create(commentCreateForm.getComment(),user,commentCreateForm.getBno());

        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete")
    public String commentDelete(@RequestParam int cno, @RequestParam int bno , Principal principal){

        if(!(this.userService.getUser(principal.getName()) == this.commentService.getComment(cno).getUserprofile()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");

        this.commentService.delete(cno);

        return "redirect:/blog/list/"+bno;
    }
}
