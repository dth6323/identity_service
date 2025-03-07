package com.hadz.identity_service.service;

import com.hadz.identity_service.dto.response.UserResponse;
import com.hadz.identity_service.entity.User;
import com.hadz.identity_service.exception.AppException;
import com.hadz.identity_service.exception.ErrorCode;
import com.hadz.identity_service.mapper.UserMapper;
import com.hadz.identity_service.repository.UserRepository;
import com.hadz.identity_service.dto.request.UserCreationRequest;
import com.hadz.identity_service.dto.request.UserUpdateRequest;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    public User createUser(UserCreationRequest request){

        if(userRepository.existsByUsername(request.getUsername())){
            throw new RuntimeException("ErrorCode.USER_EXISTED");
        }
        User user = userMapper.toUser(request);
        return userRepository.save(user);
    }
    public List<User> getUser(){
        return userRepository.findAll();
    }
    public UserResponse getUserById(String userId){
        return userMapper.toUserResponse(userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found")));
    }
    public UserResponse updateUser(String userId, UserUpdateRequest request){

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found"));
        userMapper.updateUser(user,request);
        return userMapper.toUserResponse(userRepository.save(user));
    }
    public void deleteUser(String userId){
        userRepository.deleteById(userId);
    }
}
