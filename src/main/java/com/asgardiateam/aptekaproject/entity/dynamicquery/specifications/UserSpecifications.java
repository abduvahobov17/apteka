package com.asgardiateam.aptekaproject.entity.dynamicquery.specifications;

import com.asgardiateam.aptekaproject.entity.Product;
import com.asgardiateam.aptekaproject.entity.User;
import com.asgardiateam.aptekaproject.entity.dynamicquery.criteria.UserCriteria;
import com.asgardiateam.aptekaproject.enums.BotState;
import com.asgardiateam.aptekaproject.enums.ClientType;
import com.asgardiateam.aptekaproject.enums.Lang;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public final class UserSpecifications {

    public static Specification<User> createSpecification(UserCriteria criteria) {
        return idLikeTo(criteria.getId())
                .and(firstNameLikeTo(criteria.getFirstName()))
                .and(lastNameLikeTo(criteria.getLastName()))
                .and(firstNameTelegLikeTo(criteria.getFirstNameTeleg()))
                .and(lastNameTelegLikeTo(criteria.getLastNameTeleg()))
                .and(userNameTelegLikeTo(criteria.getUserNameTeleg()))
                .and(telegramIdLikeTo(criteria.getTelegramId()))
                .and(createRegistrationTimeBetween(criteria.getStartRegisteredDate(), criteria.getEndRegisteredDate()))
                .and(clientTypeEquals(criteria.getClientType()))
                .and(langEquals(criteria.getLang()))
                .and(botStateEquals(criteria.getBotState()));
    }

    public static Specification<User> idLikeTo(Long id) {
        return (root, query, criteriaBuilder) -> id != null ?
                criteriaBuilder.equal(root.get("id"), id) : null;
    }

    public static Specification<User> firstNameLikeTo(String firstName) {
        return (root, query, criteriaBuilder) -> nonNull(firstName) ?
                criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), '%' + firstName.toLowerCase() + '%') : null;
    }

    public static Specification<User> lastNameLikeTo(String lastName) {
        return (root, query, criteriaBuilder) -> nonNull(lastName) ?
                criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), '%' + lastName.toLowerCase() + '%') : null;
    }

    public static Specification<User> firstNameTelegLikeTo(String firstNameTeleg) {
        return (root, query, criteriaBuilder) -> nonNull(firstNameTeleg) ?
                criteriaBuilder.like(criteriaBuilder.lower(root.get("firstNameTeleg")), '%' + firstNameTeleg.toLowerCase() + '%') : null;
    }

    public static Specification<User> lastNameTelegLikeTo(String lastNameTeleg) {
        return (root, query, criteriaBuilder) -> nonNull(lastNameTeleg) ?
                criteriaBuilder.like(criteriaBuilder.lower(root.get("lastNameTeleg")), '%' + lastNameTeleg.toLowerCase() + '%') : null;
    }

    public static Specification<User> userNameTelegLikeTo(String userNameTeleg) {
        return (root, query, criteriaBuilder) -> nonNull(userNameTeleg) ?
                criteriaBuilder.like(criteriaBuilder.lower(root.get("lastNameTeleg")), '%' + userNameTeleg.toLowerCase() + '%') : null;
    }

    public static Specification<User> telegramIdLikeTo(String telegramIdLikeTo) {
        return (root, query, criteriaBuilder) -> nonNull(telegramIdLikeTo) ?
                criteriaBuilder.like(criteriaBuilder.lower(root.get("telegramId")), '%' + telegramIdLikeTo.toLowerCase() + '%') : null;
    }

    public static Specification<User> createRegistrationTimeBetween(Instant startRegistration, Instant endRegistration) {
        return (root, query, criteriaBuilder) -> isNull(endRegistration) ? criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), startRegistration)
                : criteriaBuilder.between(root.get("createdDate"), startRegistration, endRegistration);
    }

    public static Specification<User> clientTypeEquals(ClientType clientType) {
        return (root, query, criteriaBuilder) -> nonNull(clientType) ?
                criteriaBuilder.equal(root.get("clientType"), clientType) : null;
    }

    public static Specification<User> langEquals(Lang lang) {
        return (root, query, criteriaBuilder) -> nonNull(lang) ?
                criteriaBuilder.equal(root.get("lang"), lang) : null;
    }

    public static Specification<User> botStateEquals(BotState botState) {
        return (root, query, criteriaBuilder) -> nonNull(botState) ?
                criteriaBuilder.equal(root.get("lang"), botState) : null;
    }

}
