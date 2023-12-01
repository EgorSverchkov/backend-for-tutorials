package ru.sverchkov.backendfortutorials.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.sverchkov.backendfortutorials.model.dto.UserDetailsImpl;
import ru.sverchkov.backendfortutorials.model.entity.UserEntity;
import ru.sverchkov.backendfortutorials.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username : " + username + " not found"));

        return UserDetailsImpl.build(userEntity);
    }
}
