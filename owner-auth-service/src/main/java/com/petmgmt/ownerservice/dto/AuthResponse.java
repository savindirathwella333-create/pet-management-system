package com.petmgmt.ownerservice.dto;

public class AuthResponse {
    private String token;
    private Long ownerId;
    private String name;
    private String email;

    public AuthResponse(String token, Long ownerId, String name, String email) {
        this.token = token;
        this.ownerId = ownerId;
        this.name = name;
        this.email = email;
    }

    public String getToken() { return token; }
    public Long getOwnerId() { return ownerId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
}
