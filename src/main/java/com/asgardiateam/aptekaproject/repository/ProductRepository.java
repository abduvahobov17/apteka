package com.asgardiateam.aptekaproject.repository;

import com.asgardiateam.aptekaproject.entity.Product;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

public interface ProductRepository extends JpaRepositoryImplementation<Product, Long> {
}
