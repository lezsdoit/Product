package com.example.demo.service;
import com.example.demo.dao.repository.MyRepository;
import com.example.demo.dao.repository.ProductRepository;
import com.example.demo.dto.ProductDto;
import com.example.demo.dao.entity.Product;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MyProductImplementation implements MyProduct{

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    MyRepository myRepository;

    @Override
    public void addProduct(MultipartFile file,Long fileId) throws IOException {

        Workbook workbook = null;

        try {
            workbook = new XSSFWorkbook(file.getInputStream());
        } catch (IOException e) {
           logger.error(e.getMessage());
           throw new IOException();
        }

        Sheet sheet = workbook.getSheetAt(0);
        List<ProductDto> productDtos = new ArrayList<>();
        ProductDto productDto;
        int i=1;
        String err="";

        while(i<sheet.getPhysicalNumberOfRows()){
            productDto = new ProductDto();
            productDto.setFileId(fileId);
            for(int j=0;j<sheet.getRow(i).getPhysicalNumberOfCells();j++){
               String error = ProductUtil.extractData(sheet.getRow(0).getCell(j).getStringCellValue(),sheet.getRow(i).getCell(j),productDto);
               if(!(error.equals("")))
               {
                   err=err+" | "+error;
               }
            }
            productDtos.add(productDto);
            i++;
        }

        if(!(err.equals("")))
        {
            logger.warn("Something unusual : "+err);
        }

        List<Product> products = productDtos.stream().map(dto->modelMapper.map(dto,Product.class)).collect(Collectors.toList());
        productRepository.saveAll(products);
    }

    @Override
    public ResponseEntity<List<ProductDto>> getProductByName(String name) {
        try {
            List<Product> products = productRepository.findByName(name);
            List<ProductDto> productDtos = products.stream().map(pd->modelMapper.map(pd,ProductDto.class)).collect(Collectors.toList());
            return ResponseEntity.ok().body(productDtos);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<String> deleteProductById(long id) {
        try {
            productRepository.deleteByFileId(id);
            return ResponseEntity.ok().body("Deleted Products Successfully");
        }
        catch (Exception e){
            logger.error("Unable to delete");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ResponseEntity<List<ProductDto>> getProductByProductId(Long id) {
        List<Product> products = productRepository.findByProductId(id);
        List<ProductDto> productDtos = products.stream().map(product -> modelMapper.map(product,ProductDto.class)).collect(Collectors.toList());
        return ResponseEntity.ok().body(productDtos);
    }

    @Override
    public ResponseEntity<List<ProductDto>> getProductByVendorId(Long id) {
        List<Product> products = productRepository.findByVendorId(id);
        List<ProductDto> productDtos = products.stream().map(product -> modelMapper.map(product,ProductDto.class)).collect(Collectors.toList());
        return ResponseEntity.ok().body(productDtos);
    }

    @Override
    public ResponseEntity<List<ProductDto>> getProductByCategory(String category) {
        List<Product> products = productRepository.findByCategory(category);
        List<ProductDto> productDtos = products.stream().map(product -> modelMapper.map(product,ProductDto.class)).collect(Collectors.toList());
        return ResponseEntity.ok().body(productDtos);
    }
 
    @Override
    public ResponseEntity<Resource> getProductByFileId(Long id) throws IOException {

        String fileName = myRepository.getById(id).getFileName();
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(fileName);
        List<String> list = Arrays.asList("product id","vendor id","product name","price","inventory","description","category");

        List<Product> products = productRepository.findByFileId(id);

        sheet.createRow(0);

        for(int i=0;i< list.size();i++){
            sheet.getRow(0).createCell(i).setCellValue(list.get(i));
        }

        for (int i=0;i< products.size();i++){
            sheet.createRow(i+1);
            for(int j=0;j< list.size();j++){
                ProductUtil.insertCell(sheet,i+1,j,list.get(j),products.get(i));
            }
        }

        String path = "/Users/amansingh/Downloads/demo/src/main/Output"+fileName;
        OutputStream fileOut = new FileOutputStream(path);
        workbook.write(fileOut);
        File file = new File(path);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachement, filename Output"+fileName)
                .body(resource);
    }
}
