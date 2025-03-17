package com.hadz.identity_service.service;

import com.hadz.identity_service.dto.response.UserResponse;
import com.hadz.identity_service.entity.User;
import com.hadz.identity_service.enums.Role;
import com.hadz.identity_service.exception.AppException;
import com.hadz.identity_service.exception.ErrorCode;
import com.hadz.identity_service.mapper.UserMapper;
import com.hadz.identity_service.repository.UserRepository;
import com.hadz.identity_service.dto.request.UserCreationRequest;
import com.hadz.identity_service.dto.request.UserUpdateRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor()
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    private UserRepository userRepository;
    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;
    public User createUser(UserCreationRequest request){

        if(userRepository.existsByUsername(request.getUsername())){
            throw new RuntimeException("ErrorCode.USER_EXISTED");
        }
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
        user.setRoles(roles);
        return userRepository.save(user);
    }
    public List<UserResponse> getUser(){
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
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
