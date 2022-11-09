package com.springboot.Teamproject.service;

import com.springboot.Teamproject.entity.BlogBoard;
import com.springboot.Teamproject.entity.ImageFile;
import com.springboot.Teamproject.entity.User;
import com.springboot.Teamproject.repository.BlogBoardRepository;
import com.springboot.Teamproject.repository.ImageFileRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BlogBoardService {

    private final BlogBoardRepository boardRepository;

    private final ImageFileRepository imageFileRepository;

    @Value("${file.dir}")
    private String fileDir;     //aplication.properties 에 있는 파일 경로 변수 저장

    //게시판 글 생성
    public void create(String title, String content,  MultipartFile files,User user) throws IOException {

        BlogBoard board = new BlogBoard();
        board.setTitle(title);
        board.setContent(content);
        board.setWriter(user.getNickname());
        board.setCreateDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss")));
        board.setUserprofile(user);

        this.boardRepository.save(board);

        if(!files.isEmpty())        //이미지 파일이 있는지 확인, 있을 경우에만 파일 정보 등록
        {
            String origName = files.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            String extension = origName.substring(origName.lastIndexOf("."));
            String savedName = uuid + extension;
            String savedPath = fileDir + savedName;

            File _file = new File(savedPath);

            //해당 경로의 폴더가 없을 경우 자동으로 폴더 생성 후 정상 작동
            if(_file.mkdirs())
                files.transferTo(_file);
            else
                files.transferTo(_file);


            ImageFile file = new ImageFile();
            file.setOriginName(origName);
            file.setSavedName(savedName);
            file.setSavedPath(savedPath);
            file.setBoard(board);

            this.imageFileRepository.save(file);
        }
    }

    //게시판 글 목록을 게시판 번호 내림차순으로 가져옴
    public List<BlogBoard> getList(){
        return this.boardRepository.findAll(Sort.by(Sort.Direction.DESC,"bno"));
    }

    //해당 번호의 게시판을 직접 가져옴
    public BlogBoard getBlog(int bno){
        Optional<BlogBoard> board = this.boardRepository.findById(bno);

        return board.get();
    }

    //게시판 수정
    public void modifyBlog(BlogBoard board , String title, String content){

        board.setTitle(title);
        board.setContent(content);

        this.boardRepository.save(board);
    }

    //게시판 삭제
    public void deleteBlog(BlogBoard board){

        this.boardRepository.delete(board);
    }

    //해당 번호의 게시판에 등록된 이미지 파일 정보를 가져옴
    public List<ImageFile> getImageFiles(int bno){

        return this.imageFileRepository.findAllByboardBno(bno);
    }
}
