package com.hadz.identity_service.controller;

import com.hadz.identity_service.entity.User;
import com.hadz.identity_service.service.UserService;
import dto.request.UserCreationRequest;
import dto.request.UserUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping
    User CreateUser(@RequestBody UserCreationRequest request){
        return userService.createUser(request);
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
}
