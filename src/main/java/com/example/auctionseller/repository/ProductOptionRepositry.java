package com.example.auctionseller.repository;

import com.example.auctionseller.model.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductOptionRepositry extends JpaRepository<ProductOption, Integer> {


    List<ProductOption> findAllByProductId(int id);
}
