package com.example.demo.service;

import com.example.demo.dto.MyFileDto;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface MyService {

    public String uploadFile(MultipartFile myFileDto) throws IOException;
    public List<MyFileDto> getAllFile();
    public ResponseEntity<org.springframework.core.io.Resource> downloadFileByName(String name) throws FileNotFoundException;
    ResponseEntity<MyFileDto> getFileById(Long id);
    ResponseEntity<String> deleteFileById(long id);
}
