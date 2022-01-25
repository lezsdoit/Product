package com.example.demo.dao.repository;

import com.example.demo.dao.entity.Product;
import com.example.demo.dao.entity.helper.ProductPK;
import com.example.demo.service.ProductUtil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product,Long> {
    public List<Product> findByName(String name);
    @Transactional
    public void deleteByFileId(Long id);
    public List<Product> findByProductId(Long id);
    public List<Product> findByVendorId(Long id);
    public List<Product> findByCategory(String name);
    public List<Product> findByFileId(Long id);
}
