package com.springboot.Teamproject.controller;

import com.springboot.Teamproject.DTO.BoardCreateForm;
import com.springboot.Teamproject.entity.BlogBoard;
import com.springboot.Teamproject.entity.Comment;
import com.springboot.Teamproject.entity.ImageFile;
import com.springboot.Teamproject.entity.User;
import com.springboot.Teamproject.service.BlogBoardService;
import com.springboot.Teamproject.service.CommentService;
import com.springboot.Teamproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/blog")
public class BlogController {

    private final BlogBoardService boardService;

    private final UserService userService;

    private final CommentService commentService;

    @GetMapping("/list")
    public String getBlogBoardList(Model model){
        List<BlogBoard> blogBoardList = this.boardService.getList();
        if(!blogBoardList.isEmpty())
        {
            model.addAttribute("blogBoardList",blogBoardList);

            int recentBno = blogBoardList.get(0).getBno();

            BlogBoard blogBoard = this.boardService.getBlog(recentBno);
            model.addAttribute("blogBoard",blogBoard);

            List<ImageFile> fileList = this.boardService.getImageFiles(recentBno);
            model.addAttribute("fileList",fileList);

            List<Comment> commentList = this.commentService.getList(recentBno);
            model.addAttribute("commentList",commentList);

            return "blog_main";
        }
        else{
            return "blog_emptiedMain";
        }
    }

    @GetMapping("/list/{bno}")
    public String getBlogBoardList(Model model,@PathVariable("bno") int bno){
        List<BlogBoard> blogBoardList = this.boardService.getList();
        model.addAttribute("blogBoardList",blogBoardList);

        BlogBoard blogBoard = this.boardService.getBlog(bno);
        model.addAttribute("blogBoard",blogBoard);

        List<ImageFile> fileList = this.boardService.getImageFiles(bno);
        model.addAttribute("fileList",fileList);

        List<Comment> commentList = this.commentService.getList(bno);
        model.addAttribute("commentList",commentList);

        return "blog_main";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String blogBoardCreate(BoardCreateForm boardCreateForm){
        return "blog_create";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String blogBoardCreate(@Valid BoardCreateForm boardCreateForm, BindingResult bindingResult,Principal principal) throws IOException {

        if(bindingResult.hasErrors())
            return "blog_create";

        User user = this.userService.getUser(principal.getName());
        this.boardService.create(boardCreateForm.getTitle(),boardCreateForm.getContent(),boardCreateForm.getFile(),user);

        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{bno}")
    public String blogBoardModify(BoardCreateForm boardCreateForm,@PathVariable Integer bno, Principal principal){

        BlogBoard board = this.boardService.getBlog(bno);

        if(!board.getUserprofile().getId().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정 권한이 없습니다.");
        }
        boardCreateForm.setTitle(board.getTitle());
        boardCreateForm.setContent(board.getContent());

        return "blog_create";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{bno}")
    public String blogBoardModify(@Valid BoardCreateForm boardCreateForm,BindingResult bindingResult,Principal principal,@PathVariable Integer bno){

        if(bindingResult.hasErrors())
            return "blog_create";

        BlogBoard board = this.boardService.getBlog(bno);

        if(!board.getUserprofile().getId().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정 권한이 없습니다.");
        }

        this.boardService.modifyBlog(board,boardCreateForm.getTitle(),boardCreateForm.getContent());

        return String.format("redirect:/blog/list/%s",bno);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{bno}")
    public String blogBoardDelete(Principal principal,@PathVariable Integer bno){

        BlogBoard board = this.boardService.getBlog(bno);
        if(!board.getUserprofile().getId().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"삭제 권한이 없습니다.");

        this.boardService.deleteBlog(board);

        return "redirect:/";
    }
}
