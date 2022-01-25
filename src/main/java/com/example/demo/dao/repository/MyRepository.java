package com.example.demo.dao.repository;

import com.example.demo.dao.entity.FileData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyRepository extends JpaRepository<FileData,Long>{
}
