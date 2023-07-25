package com.amigoscode.authentication;

public record AuthenticationRequest(
        String username,
        String password
) {
}
