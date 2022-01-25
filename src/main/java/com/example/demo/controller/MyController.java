package com.example.demo.controller;
import com.example.demo.dto.MyFileDto;
import com.example.demo.dto.ProductDto;
import com.example.demo.service.MyProduct;
import com.example.demo.service.MyService;
import org.apache.coyote.Response;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
public class MyController {

    @Autowired
    private MyService myService;
    @Autowired
    private MyProduct myProductService;

    @PostMapping("/files")
    public ResponseEntity<String> upload(@RequestParam("file")MultipartFile file) throws IOException {
        String response = myService.uploadFile(file);
        if(response.equals("Error")){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.of(Optional.of(response));
    }

    @GetMapping("/files")
    public ResponseEntity<List<MyFileDto>> getAllFile(){

        List<MyFileDto> list = myService.getAllFile();

        if(list.size()==0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(list);
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<MyFileDto> getFileById(@PathVariable Long id){
        return myService.getFileById(id);
    }

    @GetMapping("/files/file-by-name/{name}")
    public ResponseEntity<org.springframework.core.io.Resource> downloadFileByName(@PathVariable String name) throws FileNotFoundException {
       return myService.downloadFileByName(name);
    }

    @GetMapping("/files/prod-by-fileId/{id}")
    public ResponseEntity<Resource> getProductByFileId(@PathVariable Long id) throws IOException {
        return myProductService.getProductByFileId(id);
    }

    @GetMapping("/files/product-by-name/{name}")
    public ResponseEntity<List<ProductDto>> getProductByName(@PathVariable String name){
        return myProductService.getProductByName(name);
    }

    @GetMapping("/files/product-by-pid/{id}")
    public ResponseEntity<List<ProductDto>> getProductByProductId(@PathVariable Long id){
        return myProductService.getProductByProductId(id);
    }

    @GetMapping("/files/product-by-vid/{id}")
    public ResponseEntity<List<ProductDto>> getProductByVendorId(@PathVariable Long id){
        return myProductService.getProductByVendorId(id);
    }

    @GetMapping("/files/product-by-cat/{category}")
    public ResponseEntity<List<ProductDto>> getProductByCategory(@PathVariable String category){
        return myProductService.getProductByCategory(category);
    }

    @DeleteMapping("/files/file-by-id/{id}")
    public ResponseEntity<String> deleteFileById(@PathVariable long id){
        return myService.deleteFileById(id);
    }

    @DeleteMapping("/files/product-by-fileid/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable long id){
        return myProductService.deleteProductById(id);
    }
}