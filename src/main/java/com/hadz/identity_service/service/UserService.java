package com.hadz.identity_service.service;

import com.hadz.identity_service.entity.User;
import com.hadz.identity_service.repository.UserRepository;
import dto.request.UserCreationRequest;
import dto.request.UserUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(UserCreationRequest request){
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDob(request.getDob());
        return userRepository.save(user);
    }
    public List<User> getUser(){
        return userRepository.findAll();
    }
    public User getUserById(String userId){
        return userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found"));
    }
    public User updateUser(String userId, UserUpdateRequest request){
        User user = getUserById(userId);
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDob(request.getDob());
        return userRepository.save(user);
    }
    public void deleteUser(String userId){
        userRepository.deleteById(userId);
    }
}
