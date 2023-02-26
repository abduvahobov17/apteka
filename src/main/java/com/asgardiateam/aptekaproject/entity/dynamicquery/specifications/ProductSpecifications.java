package com.asgardiateam.aptekaproject.entity.dynamicquery.specifications;

import com.asgardiateam.aptekaproject.entity.Product;
import com.asgardiateam.aptekaproject.entity.dynamicquery.criteria.ProductCriteria;
import com.asgardiateam.aptekaproject.enums.UnitType;
import org.springframework.data.jpa.domain.Specification;

import static java.util.Objects.nonNull;

public final class ProductSpecifications {

    public static Specification<Product> createSpecifications(ProductCriteria productCriteria) {
        return idLikeTo(productCriteria.getId())
                .and(nameLikeTo(productCriteria.getName()))
                .and(amountAround(productCriteria.getFromAmount(), productCriteria.getToAmount()))
                .and(descriptionLikeTo(productCriteria.getDescription()))
                .and(priceAround(productCriteria.getFromPrice(), productCriteria.getToPrice()))
                .and(unitTypeEquals(productCriteria.getUnitType()));
    }

    public static Specification<Product> idLikeTo(Long id) {
        return (root, query, criteriaBuilder) -> id != null ?
                criteriaBuilder.equal(root.get("id"), id) : null;
    }

    public static Specification<Product> nameLikeTo(String name) {
        return (root, query, criteriaBuilder) -> nonNull(name) ?
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), '%' + name.toLowerCase() + '%') : null;
    }

    public static Specification<Product> amountAround(Long fromAmount, Long toAmount) {
        return (root, query, criteriaBuilder) -> toAmount != null ?
                criteriaBuilder.between(root.get("amount"), fromAmount, toAmount) :
                criteriaBuilder.greaterThanOrEqualTo(root.get("amount"), fromAmount);
    }

    public static Specification<Product> descriptionLikeTo(String description) {
        return (root, query, criteriaBuilder) -> nonNull(description) ?
                criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), '%' + description + '%') : null;
    }

    public static Specification<Product> priceAround(Long fromPrice, Long toPrice) {
        return (root, query, criteriaBuilder) -> toPrice != null ?
                criteriaBuilder.between(root.get("price"), fromPrice, toPrice) :
                criteriaBuilder.greaterThanOrEqualTo(root.get("price"), fromPrice);
    }

    public static Specification<Product> unitTypeEquals(UnitType unitType) {
        return (root, query, criteriaBuilder) -> unitType != null ?
                criteriaBuilder.equal(root.get("unitType"), unitType.name()) : null;
    }
}
