package com.example.demo.dto;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import java.util.Date;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private Long productId;
    private Long vendorId;
    private String name;
    private Double price;
    private Long inventory;
    private String description;
    private String category;
    @Column(name = "file_id")
    private Long fileId;
    @Column(name = "created_on")
    @CreationTimestamp
    private Date createdOn;
    @UpdateTimestamp
    @Column(name = "updated_on")
    private Date updatedOn;

}
