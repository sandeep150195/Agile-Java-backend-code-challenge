package com.optimal.virtualemp.repository;

import com.optimal.virtualemp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {}