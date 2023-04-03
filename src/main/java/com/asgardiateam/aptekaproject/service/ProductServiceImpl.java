package com.asgardiateam.aptekaproject.service;

import com.asgardiateam.aptekaproject.entity.Product;
import com.asgardiateam.aptekaproject.entity.dynamicquery.criteria.ProductCriteria;
import com.asgardiateam.aptekaproject.entity.dynamicquery.specifications.ProductSpecifications;
import com.asgardiateam.aptekaproject.enums.State;
import com.asgardiateam.aptekaproject.exception.AptekaException;
import com.asgardiateam.aptekaproject.mapper.ProductMapper;
import com.asgardiateam.aptekaproject.payload.ProductDTO;
import com.asgardiateam.aptekaproject.payload.request.ProductRequest;
import com.asgardiateam.aptekaproject.repository.ProductRepository;
import com.asgardiateam.aptekaproject.service.interfaces.ProductService;
import com.asgardiateam.aptekaproject.utils.Page2Dto;
import com.asgardiateam.aptekaproject.utils.PageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;


    @Override
    public Optional<Product> findByName(String name) {
        return productRepository.findByNameAndState(name, State.ALIVE);
    }

    @Override
    public ProductDTO create(ProductRequest productRequest) {
        return productMapper.toDTO(save(productMapper.toCreate(productRequest)));
    }

    @Override
    public ProductDTO update(ProductRequest productRequest, Long id) {
        return productMapper.toDTO(save(productMapper.toUpdate(productRequest, findById(id))));
    }

    @Override
    public ProductDTO getById(Long id) {
        return productMapper.toDTO(findById(id));
    }

    @Override
    public PageDto<ProductDTO> getAll(Pageable pageable, ProductCriteria criteria) {
        return Page2Dto.toDTO(findAll(pageable, criteria).map(productMapper::toDTO));
    }

    @Override
    public void deleteById(Long id) {
        delete(findById(id));
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findByIdAndState(id, State.ALIVE).orElseThrow(AptekaException::productNotFound);
    }

    @Override
    public Page<Product> findAll(Pageable pageable, ProductCriteria productCriteria) {
        return productRepository.findAll(ProductSpecifications.createSpecifications(productCriteria), pageable);
    }

    @Override
    public Product save(Product product) {
        try {
            return productRepository.save(product);
        } catch (Exception e) {
            throw AptekaException.productSaveError();
        }
    }

    @Override
    public void delete(Product product) {
        try {
            product.setState(State.DELETED);
            productRepository.save(product);
        } catch (Exception e) {
            throw AptekaException.productDeleteError();
        }
    }
}
