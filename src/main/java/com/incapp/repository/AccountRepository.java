package com.incapp.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incapp.entity.Account;
import com.incapp.entity.User;

public interface AccountRepository extends JpaRepository<Account, Long>{

	Optional<Account> findByUserUsername(String username);
	
	boolean existsByUser(User user);
	
	Optional<Account> findByIdAndUserUsername(Long id, String username);
	
	boolean existsByUserId(Long userId);
}
