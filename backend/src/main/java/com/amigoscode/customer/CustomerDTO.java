package com.amigoscode.customer;

import java.util.List;

public record CustomerDTO(
        Integer id,
        String name,
        String email,
        Gender gender,
        Integer age,
        List<String> role,
        String username

) {
}
