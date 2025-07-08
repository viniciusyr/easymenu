package com.easymenu.user;

import com.easymenu.user.enums.UserRole;
import com.easymenu.user.enums.UserStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.UUID;

public class UserSpecs {

    public static Specification<UserModel> hasId(UUID providedId){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("id"), providedId);
    }

    public static Specification<UserModel> containsName(String providedName){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + providedName.toLowerCase() + "%");
    }

    public static Specification<UserModel> containsEmail(String providedEmail){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + providedEmail.toLowerCase() + "%");
    }

    public static Specification<UserModel> hasStatus(UserStatus providedStatus){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), providedStatus);
    }

    public static Specification<UserModel> hasRole(UserRole providedRole){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("role"), providedRole);
    }

    public static Specification<UserModel> createdAfter(LocalDate start) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("createdOn"), start.atStartOfDay());
    }

    public static Specification<UserModel> createdBefore(LocalDate end) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("createdOn"), end.atTime(23, 59, 59));
    }

    public static Specification<UserModel> betweenDates(LocalDate start, LocalDate end){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("createdOn"), start.atStartOfDay(), end.atTime(23, 59, 59));
    }

    public static Specification<UserModel> updatedOn(LocalDate date) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("updatedOn"), date.atStartOfDay(), date.atTime(23, 59, 59));
    }

}
