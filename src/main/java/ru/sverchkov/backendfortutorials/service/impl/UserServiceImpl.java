package ru.sverchkov.backendfortutorials.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.sverchkov.backendfortutorials.exception.EmptyUserIdException;
import ru.sverchkov.backendfortutorials.exception.UserNotFoundException;
import ru.sverchkov.backendfortutorials.model.entity.UserEntity;
import ru.sverchkov.backendfortutorials.model.request.UserUpdateRequest;
import ru.sverchkov.backendfortutorials.model.response.UserResponse;
import ru.sverchkov.backendfortutorials.repository.UserRepository;
import ru.sverchkov.backendfortutorials.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserResponse updateUser(UserUpdateRequest request, String userId) {
        if(userId == null) throw new EmptyUserIdException();

        UserEntity userEntity = userRepository
                .findById(UUID.fromString(userId))
                .orElseThrow(() -> new UsernameNotFoundException(userId));

        userEntity.setBirthday(request.getBirthday());
        userEntity.setAvatar(request.getAvatar());
        userEntity.setFirstName(request.getFirstName());
        userEntity.setLastName(request.getLastName());
        userEntity.setMiddleName(request.getMiddleName());

        return buildUserResponseFromEntity(userRepository.saveAndFlush(userEntity));
    }

    @Override
    public UserResponse loadUserById(String id) {
        UserEntity userEntity = userRepository
                .findById(UUID.fromString(id))
                .orElseThrow(() -> new UserNotFoundException(id));
        return buildUserResponseFromEntity(userEntity);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<UserResponse> userResponses = new ArrayList<>();
        userRepository.findAll().forEach(userEntity -> userResponses.add(buildUserResponseFromEntity(userEntity)));
        return userResponses;
    }

    private UserResponse buildUserResponseFromEntity(UserEntity userEntity) {
        return UserResponse.builder()
                .id(userEntity.getId().toString())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .birthday(userEntity.getBirthday())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .middleName(userEntity.getMiddleName())
                .avatar(userEntity.getAvatar())
                .roles(userEntity.getRoles()
                        .stream()
                        .map(role -> role.getName().toString())
                        .toList())
                .build();
    }
}
