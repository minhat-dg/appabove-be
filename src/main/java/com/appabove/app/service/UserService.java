package com.appabove.app.service;

import com.appabove.app.dto.request.LoginRequest;
import com.appabove.app.dto.request.SignupRequest;
import com.appabove.app.dto.response.BaseResponse;
import com.appabove.app.dto.response.LoginResponse;
import com.appabove.app.model.User;
import com.appabove.app.repository.UserRepository;
import com.appabove.app.utils.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    UserRepository userRepo;
    PasswordEncoder passwordEncoder;
    JwtUtil jwtUtil;

    public UserService(UserRepository userRepo, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public BaseResponse<Void> signup(SignupRequest req) {
        if (userRepo.existsByEmail(req.getEmail())) return BaseResponse.error("User already exists!");

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        userRepo.save(user);
        return BaseResponse.success("Sign Up Success!");
    }

    public BaseResponse<LoginResponse> login(LoginRequest req) {
        User user = userRepo.findByEmail(req.getEmail()).orElse(null);
        if (user == null || !passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            return BaseResponse.error("Login Failed!");
        }
        String token = jwtUtil.generateToken(user);
        return BaseResponse.success("Login Success!", new LoginResponse(token, user.getId(), user.getEmail()));
    }
}

