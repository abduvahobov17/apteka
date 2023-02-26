package com.asgardiateam.aptekaproject.repository;

import com.asgardiateam.aptekaproject.entity.Admin;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import java.util.Optional;

public interface AdminRepository extends JpaRepositoryImplementation<Admin, Long> {

    Optional<Admin> findByLogin(String login);

}
