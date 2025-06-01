package com.satwik.splitora.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    // TODO : work is needed for admin

     @GetMapping
     public ResponseEntity<String> getAdminPage() {
         return ResponseEntity.ok("Welcome to the admin page!");
     }
}
