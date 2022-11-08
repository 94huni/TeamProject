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
    private String fileDir;

    public void create(String title, String content,  MultipartFile files,User user) throws IOException {

        BlogBoard board = new BlogBoard();
        board.setTitle(title);
        board.setContent(content);
        board.setWriter(user.getNickname());
        board.setCreateDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss")));
        board.setUserprofile(user);

        this.boardRepository.save(board);

        if(files.isEmpty())
            this.imageFileRepository.save(null);

        else {
            String origName = files.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            String extension = origName.substring(origName.lastIndexOf("."));
            String savedName = uuid + extension;
            String savedPath = fileDir + savedName;

            File _file = new File(savedPath);

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

    public List<BlogBoard> getList(){
        return this.boardRepository.findAll(Sort.by(Sort.Direction.DESC,"bno"));
    }

    public BlogBoard getBlog(int bno){
        Optional<BlogBoard> board = this.boardRepository.findById(bno);

        return board.get();
    }

    public void modifyBlog(BlogBoard board , String title, String content){

        board.setTitle(title);
        board.setContent(content);

        this.boardRepository.save(board);
    }

    public void deleteBlog(BlogBoard board){

        this.boardRepository.delete(board);
    }

    public List<ImageFile> getImageFiles(int bno){

        return this.imageFileRepository.findAllByboardBno(bno);
    }
}
