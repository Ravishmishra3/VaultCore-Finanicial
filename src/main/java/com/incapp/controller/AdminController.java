package com.incapp.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incapp.dto.UserAccountStatusDto;
import com.incapp.repository.AccountRepository;
import com.incapp.repository.UserRepository;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

	private final UserRepository userRepo;
    private final AccountRepository accountRepo;

    public AdminController(UserRepository userRepo, AccountRepository accountRepo) {
        this.userRepo = userRepo;
        this.accountRepo = accountRepo;
    }

    @GetMapping("/users")
    public List<UserAccountStatusDto> getUsers() {
        return userRepo.findAll().stream()
            .filter(u -> u.getRole().equals("USER"))
            .map(u -> new UserAccountStatusDto(
                u.getId(),
                u.getUsername(),
                u.getRole(),
                accountRepo.existsByUserId(u.getId())
            ))
            .toList();
    }
}
