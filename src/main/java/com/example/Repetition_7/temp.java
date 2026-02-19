package com.example.Repetition_7;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class temp {
    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("Admin123!"));
    }
}
