package com.hadz.identity_service.controller;

import com.hadz.identity_service.dto.request.ApiResponse;
import com.hadz.identity_service.entity.User;
import com.hadz.identity_service.service.UserService;
import com.hadz.identity_service.dto.request.UserCreationRequest;
import com.hadz.identity_service.dto.request.UserUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    List<User> getAllUsers(){
        return userService.getUser();
    }
    @GetMapping("/{userId}")
    User getUserById(@PathVariable String userId){
        return userService.getUserById(userId);
    }
    @PutMapping("/{userId}")
    User updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request){
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
