package dev.hafil.demospringboot.controller;

import dev.hafil.demospringboot.model.User;
import dev.hafil.demospringboot.service.UserService;
import dev.hafil.demospringboot.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    @PostMapping("/register")
    public ResponseEntity<String > registerUser(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = passwordEncoder.encode(body.get("password"));
        String email = body.get("email");

        if (userService.findUserByUsername(username).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }
        if(userService.findByEmail(email).isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
        }
        userService.createUser(User.builder().username(username).password(password).email(email).build());
        return ResponseEntity.status(HttpStatus.CREATED).body("User created");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        if(userService.findUserByUsername(username).isEmpty()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username not found");
        }
        User user = userService.findUserByUsername(username).get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password");
        }
        String token = jwtUtil.generateToken(username);
        return ResponseEntity.ok(Map.of("token", token));
    }

}
