package com.incapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserAccountStatusDto {

	private Long userId;
    private String username;
    private String role;
    private boolean hasAccount;
    

    
    
}
