package com.example.demo.service;
import com.example.demo.dao.entity.FileData;
import com.example.demo.dao.repository.MyRepository;
import com.example.demo.dto.MyFileDto;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MyServiceImplementation implements MyService{

    private Logger logger = LoggerFactory.getLogger(MyServiceImplementation.class);
    private String fp = "/Users/amansingh/Downloads/demo/src/main/";
    @Autowired
    MyRepository myRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    MyProduct myProduct;

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        String path = "src/main/" + file.getOriginalFilename();
        File convertFile = new File(path);
        convertFile.createNewFile();

        try (FileOutputStream fileOutputStream = new FileOutputStream(convertFile)) {
            fileOutputStream.write(file.getBytes());
        }
        catch (Exception exception){
            System.out.println(exception);
        }

        MyFileDto myFileDto = new MyFileDto();
        myFileDto.setFileName(file.getOriginalFilename());
        myFileDto.setPath(path);

        FileData fileData = modelMapper.map(myFileDto, FileData.class);
        try {
            myRepository.save(fileData);
        }
        catch (Exception exception){
            logger.error("Not able to save file Meta Data");
            return "Error";
        }

        String st = fileData.getFileName()+ " with field id "+String.valueOf(fileData.getId())+" is saved";

        try {
            myProduct.addProduct(file, fileData.getId());
        }
        catch (Exception e){
            logger.error("Not able to dump rows from xlsx to Database");
            System.out.println(e.getMessage());
        }
        return st;
    }

    @Override
    public List<MyFileDto> getAllFile() {
            List<FileData> list = myRepository.findAll();
            List<MyFileDto> myFileDtos = list.stream().map(myFiledata -> modelMapper.map(myFiledata,MyFileDto.class)).collect(Collectors.toList());
            return myFileDtos;
    }

    @Override
    public ResponseEntity<MyFileDto> getFileById(Long id) {
        MyFileDto myFileDto=null;
        try {
            FileData fileData = myRepository.getById(id);
            myFileDto = modelMapper.map(fileData,MyFileDto.class);
        }
        catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok().body(myFileDto);
    }

    @Override
    public ResponseEntity<String> deleteFileById(long id) {
        String fileName = myRepository.getById(id).getFileName();
        String filePath = fp+fileName;
        try {
            Files.delete(Path.of(filePath));
        } catch (IOException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Not able to delete");
        }
        myRepository.deleteById(id);
        myProduct.deleteProductById(id);
        return ResponseEntity.ok().body("Deleted Everything Successfully");
    }

    @Override
    public ResponseEntity<Resource> downloadFileByName(String fileName) throws FileNotFoundException {
        String filePath = fp+fileName;
        File file = new File(filePath);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        System.out.println(resource);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName="+resource.getFilename())
                .body(resource);
    }
}
