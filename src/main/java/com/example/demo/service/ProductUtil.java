package com.example.demo.service;

import com.example.demo.dao.entity.Product;
import com.example.demo.dto.ProductDto;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

public class ProductUtil {
    public static String extractData(String field,Cell entry,ProductDto productDto){
        String error="";

        switch (field){
            case "product id":  long productId = (long) entry.getNumericCellValue();
                                productDto.setProductId(productId);
                                break;

            case "vendor id":    productDto.setVendorId((long) entry.getNumericCellValue());
                                break;

            case "product name": productDto.setName(entry.getStringCellValue());
                                break;
            case "price":       double price =  entry.getNumericCellValue();
                                if(price<0){error = "Price Cannot be negative";}
                                productDto.setPrice(price);
                                break;
            case "inventory":   long inventory = (long) entry.getNumericCellValue();
                                if(inventory<0){error = "Inventory cannot be negative";}
                                productDto.setInventory(inventory);
                                break;
            case "description": productDto.setDescription(entry.getStringCellValue());
                                break;
            case "category":    productDto.setCategory(entry.getStringCellValue());
                                break;
        }
        return error;
    }

    public static void insertCell(Sheet sheet, int i, int j, String s, Product product) {
        switch (s){
            case "product id":  sheet.getRow(i).createCell(j).setCellValue(product.getProductId());
                                break;
            case "vendor id":   sheet.getRow(i).createCell(j).setCellValue(product.getVendorId());
                                break;
            case "product name": sheet.getRow(i).createCell(j).setCellValue(product.getName());
                                break;
            case "price":       sheet.getRow(i).createCell(j).setCellValue(product.getPrice());
                                break;
            case "inventory":   sheet.getRow(i).createCell(j).setCellValue(product.getInventory());
                                break;
            case "description": sheet.getRow(i).createCell(j).setCellValue(product.getDescription());
                                break;
            case "category":    sheet.getRow(i).createCell(j).setCellValue(product.getCategory());
                                break;
        }
    }
}
