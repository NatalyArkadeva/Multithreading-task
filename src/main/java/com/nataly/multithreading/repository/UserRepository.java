package com.nataly.multithreading.repository;

import com.nataly.multithreading.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
