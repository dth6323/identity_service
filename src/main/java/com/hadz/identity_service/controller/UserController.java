package com.hadz.identity_service.controller;

import com.hadz.identity_service.dto.request.ApiResponse;
import com.hadz.identity_service.dto.response.UserResponse;
import com.hadz.identity_service.entity.User;
import com.hadz.identity_service.exception.ErrorCode;
import com.hadz.identity_service.service.UserService;
import com.hadz.identity_service.dto.request.UserCreationRequest;
import com.hadz.identity_service.dto.request.UserUpdateRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping
    ApiResponse<User> CreateUser(@RequestBody @Valid UserCreationRequest request){
        ApiResponse<User> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.createUser(request));
        return apiResponse;
    }
    @GetMapping
    ApiResponse<List<UserResponse>> getAllUsers(){
        var authen = SecurityContextHolder.getContext().getAuthentication();
        log.info("Username: {}", authen.getName());
        authen.getAuthorities().forEach(g -> log.info(g.getAuthority()));

        return ApiResponse.<List<UserResponse>>builder()
                .code(1000)
                .result(userService.getUser())
                .build();
    }
    @GetMapping("/{userId}")
    UserResponse getUserById(@PathVariable String userId){
        return userService.getUserById(userId);
    }
    @PutMapping("/{userId}")
    UserResponse updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request){
        return userService.updateUser(userId, request);
    }
    @DeleteMapping("/{userId}")
    void deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
    }
    @GetMapping("/test")
    public String test() {
        throw new RuntimeException("Test exception");
    }
}
