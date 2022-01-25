package com.example.demo.dao.entity;

import com.example.demo.dao.entity.helper.ProductPK;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long pid;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "vendor_id")
    private Long vendorId;
    private String name;
    private Double price;
    private Long inventory;
    private String description;
    private String category;

    @Column(name = "file_id")
    private Long fileId;

    @CreationTimestamp
    @Column(name = "created_on")
    private Date createdOn;

    @UpdateTimestamp
    @Column(name = "updated_on")
    private Date updatedOn;
}
