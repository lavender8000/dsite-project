package com.lav.dsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lav.dsite.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

}
