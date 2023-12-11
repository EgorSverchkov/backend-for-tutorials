package ru.sverchkov.backendfortutorials.service.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.sverchkov.backendfortutorials.model.dto.ERole;
import ru.sverchkov.backendfortutorials.model.entity.RoleEntity;
import ru.sverchkov.backendfortutorials.model.entity.UserEntity;
import ru.sverchkov.backendfortutorials.model.request.SignupRequest;
import ru.sverchkov.backendfortutorials.model.response.MessageResponse;
import ru.sverchkov.backendfortutorials.repository.RoleRepository;
import ru.sverchkov.backendfortutorials.repository.UserRepository;
import ru.sverchkov.backendfortutorials.service.RegisterUserService;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class RegisterUserServiceImpl implements RegisterUserService {
    private static final String ROLE_IS_NOT_FOUND = "Error! Role is not found";
    private static final String MOD_ROLE = "MODERATOR";
    private static final String ADMIN_ROLE = "ADMIN";

    private static final String ERROR_USERNAME = "Error! Username already taken.";
    private static final String ERROR_EMAIL = "Error! Email already taken.";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public ResponseEntity<?> registerUser(SignupRequest signupRequest) {
        log.info("register user is called with signupRequest : {}", signupRequest);
        if(userRepository.existsByUsername(signupRequest.getUsername()))
            return ResponseEntity.badRequest().body(new MessageResponse(ERROR_USERNAME));

        if(userRepository.existsByEmail(signupRequest.getEmail()))
            return ResponseEntity.badRequest().body(new MessageResponse(ERROR_EMAIL));


        UserEntity user = UserEntity.builder()
                .email(signupRequest.getEmail())
                .username(signupRequest.getUsername())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .build();

        log.info("UserEntity user is : {}", user);

        Set<String> strRoles = signupRequest.getRole();

        Set<RoleEntity> roles = new HashSet<>();

        if(strRoles == null || strRoles.isEmpty()) {
            RoleEntity roleEntity = roleRepository.findByName(ERole.USER)
                    .orElseThrow(() -> new RuntimeException(ROLE_IS_NOT_FOUND));
            roles.add(roleEntity);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case ADMIN_ROLE :
                        RoleEntity adminEntity = roleRepository
                                .findByName(ERole.ADMIN).orElseThrow(() -> new RuntimeException(ROLE_IS_NOT_FOUND));
                        roles.add(adminEntity);

                        break;
                    case MOD_ROLE :
                        RoleEntity moderatorEntity = roleRepository
                                .findByName(ERole.MODERATOR).orElseThrow(() -> new RuntimeException(ROLE_IS_NOT_FOUND));
                        roles.add(moderatorEntity);

                        break;
                    default:
                        RoleEntity userEntity = roleRepository
                                .findByName(ERole.USER).orElseThrow(() -> new RuntimeException(ROLE_IS_NOT_FOUND));
                        roles.add(userEntity);
                }
            });
        }

        log.info("RoleEntity roles is : {}", roles);

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Пользователь зарегестрирован успешно!"));
    }
}
