package com.example.demo.controller;

import com.example.demo.model.LoginRequestDTO;
import com.example.demo.model.SignupRequestDTO;
import com.example.demo.model.User;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
//@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;

    @Autowired
    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        String email = request.getEmail();
        String password = request.getPassword();

        User user = userRepository.findByEmail(email);

        if (user == null) {
            Map<String, String> response = new HashMap<>();
            response.put("Error","User does not exist");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } else if (!user.getPassword().equals(password)) {
            Map<String, String> response = new HashMap<>();
            response.put("Error","Username/Password Incorrect");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        return ResponseEntity.ok("Login Successful");
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDTO request) {
        String email = request.getEmail();

        if (userRepository.findByEmail(email) != null) {
            Map<String, String> response = new HashMap<>();
            response.put("Error","Forbidden, Account already exists");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        User newUser = new User();
        newUser.setName(request.getName());
        newUser.setEmail(email);
        newUser.setPassword(request.getPassword());

        userRepository.save(newUser);

        return ResponseEntity.ok("Account Creation Successful");
    }
}
