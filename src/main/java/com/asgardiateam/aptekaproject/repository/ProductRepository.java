package com.asgardiateam.aptekaproject.repository;

import com.asgardiateam.aptekaproject.entity.Product;
import com.asgardiateam.aptekaproject.enums.State;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import java.util.Optional;

public interface ProductRepository extends JpaRepositoryImplementation<Product, Long> {
    Optional<Product> findByIdAndState(Long id, State alive);

    Optional<Product> findByNameAndState(String name, State state);
}
