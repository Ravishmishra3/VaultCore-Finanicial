package com.incapp.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.incapp.entity.User;
import com.incapp.repository.UserRepository;

@Service
public class AuthService implements UserDetailsService{

	
	@Autowired
	UserRepository repo;
	
	//or
	
//	private final UserRepository repo;
//
//    public AuthService(UserRepository repo) {
//        this.repo = repo;
//    }
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User user = repo.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User not found: " + username));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole()) // DB value: ADMIN / USER
                .build();
    }
}
