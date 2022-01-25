package com.example.demo.service;

import com.example.demo.dto.ProductDto;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface MyProduct {
    public void addProduct(MultipartFile file,Long fileId) throws IOException;
    ResponseEntity<List<ProductDto>> getProductByName(String name);
    ResponseEntity<String> deleteProductById(long id);
    ResponseEntity<List<ProductDto>> getProductByProductId(Long id);
    ResponseEntity<List<ProductDto>> getProductByVendorId(Long id);
    ResponseEntity<List<ProductDto>> getProductByCategory(String category);
    ResponseEntity<Resource> getProductByFileId(Long id) throws IOException;
}
