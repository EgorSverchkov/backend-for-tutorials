package ru.sverchkov.backendfortutorials.service;

import ru.sverchkov.backendfortutorials.model.request.UserUpdateRequest;
import ru.sverchkov.backendfortutorials.model.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse updateUser(UserUpdateRequest request, String userId);
    UserResponse loadUserById(String id);
    List<UserResponse> getAllUsers();
}
