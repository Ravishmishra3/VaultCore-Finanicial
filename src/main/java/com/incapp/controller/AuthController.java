package com.incapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incapp.config.JwtUtil;
import com.incapp.dto.LoginRequest;
import com.incapp.dto.RegisterRequest;
import com.incapp.entity.User;
import com.incapp.repository.UserRepository;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	JwtUtil jwtUtil;
	
	@Autowired
	AuthenticationManager authManager;
	
	@Autowired
    private UserRepository userRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;

	
	@PostMapping("/login")
	public String login(@RequestBody LoginRequest req) {
		authManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
		
		
		return jwtUtil.generateToken(req.getUsername());
	}
	
	@PostMapping("/register")
	public String register(@RequestBody RegisterRequest req) {
	    // 1. Check if user already exists
	    if (userRepository.findByUsername(req.getUsername()).isPresent()) {
	        return "Username already taken";
	    }

	    // 2. Hash the password
	    String hashedPassword = passwordEncoder.encode(req.getPassword());

	    // 3. Save the new user
	    User newUser = new User();
	    newUser.setUsername(req.getUsername());
	    newUser.setPassword(hashedPassword);
	    newUser.setRole(req.getRole()); // e.g., "USER"
	    userRepository.save(newUser);

	    return "User registered successfully";
	}

	
}
