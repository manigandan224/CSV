package com.example.CSVOperation.CSVRepository;

import com.example.CSVOperation.CSVEntity.CSVModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CSVService extends JpaRepository<CSVModel,Integer> {

}
