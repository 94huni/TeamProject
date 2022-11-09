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

    //게시판 목록 기능
    @GetMapping("/list")
    public String getBlogBoardList(Model model){
        List<BlogBoard> blogBoardList = this.boardService.getList();
        if(!blogBoardList.isEmpty())
        {
            //html 에서 정보를 가져오기 위해 연동 시켜주는 모델
            model.addAttribute("blogBoardList",blogBoardList);

            //가장 최근에 올라온 게시글의 정보를 받아옴
            int recentBno = blogBoardList.get(0).getBno();

            //게시글 번호를 통해 정보를 받아온뒤 모델과 연동
            BlogBoard blogBoard = this.boardService.getBlog(recentBno);
            model.addAttribute("blogBoard",blogBoard);

            //게시글 번호를 통해 파일 정보 목록을 가져온 뒤 모델과 연동
            List<ImageFile> fileList = this.boardService.getImageFiles(recentBno);
            model.addAttribute("fileList",fileList);

            //게시글 번호를 통해 댓글 정보 목록을 가져온 뒤 모델과 연동
            List<Comment> commentList = this.commentService.getList(recentBno);
            model.addAttribute("commentList",commentList);

            return "blog_main";     //해당 이름을 가진 홈페이지로 이동
        }
        else{       //게시글이 하나도 없을 경우 다른 홈페이지로 이동
            return "blog_emptiedMain";
        }
    }

    //게시글 목록에서 제목을 클릭했을 때 해당 번호의 게시글 정보를 가져오기 위한 매핑
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

    //게시글 생성 홈페이지로 이동
    @PreAuthorize("isAuthenticated()")      //로그인 했는지 유무를 확인
    @GetMapping("/create")
    public String blogBoardCreate(BoardCreateForm boardCreateForm){
        return "blog_create";
    }

    //게시글 등록 시 정보를 보내주는 기능
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String blogBoardCreate(@Valid BoardCreateForm boardCreateForm, BindingResult bindingResult,Principal principal) throws IOException {

        if(bindingResult.hasErrors())
            return "blog_create";

        User user = this.userService.getUser(principal.getName());      //현재 로그인한 유저의 id를 가져와 유저 정보를 찾아옴
        this.boardService.create(boardCreateForm.getTitle(),boardCreateForm.getContent(),boardCreateForm.getFile(),user);

        return "redirect:/";
    }

    //게시글 수정 홈페이지로 이동
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

    //수정 된 게시글 등록 하는 기능
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{bno}")
    public String blogBoardModify(@Valid BoardCreateForm boardCreateForm,BindingResult bindingResult,Principal principal,@PathVariable Integer bno){

        if(bindingResult.hasErrors())
            return "blog_create";

        BlogBoard board = this.boardService.getBlog(bno);

        if(!board.getUserprofile().getId().equals(principal.getName()))       //현재 접속한 유저와 게시글에 등록 한 유저의 id값을 비교
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정 권한이 없습니다.");


        this.boardService.modifyBlog(board,boardCreateForm.getTitle(),boardCreateForm.getContent());

        return String.format("redirect:/blog/list/%s",bno);
    }

    //게시글 삭제 기능
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{bno}")
    public String blogBoardDelete(Principal principal,@PathVariable Integer bno){

        BlogBoard board = this.boardService.getBlog(bno);
        if(!board.getUserprofile().getId().equals(principal.getName()))     //현재 접속한 유저와 게시글에 등록 한 유저의 id값을 비교
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"삭제 권한이 없습니다.");

        this.boardService.deleteBlog(board);

        return "redirect:/";
    }
}
