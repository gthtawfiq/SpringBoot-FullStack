package com.amigoscode.authentication;

import com.amigoscode.customer.CustomerDTO;

public record AuthenticationResponse(
        String token,
        CustomerDTO customerDTO

) {
}
