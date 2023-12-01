package ru.sverchkov.backendfortutorials.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.sverchkov.backendfortutorials.model.entity.UserEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Data
public class UserDetailsImpl implements UserDetails {
    private UUID id;

    private String username;

    private String email;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(UUID id
            , String username, String email
            , String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(UserEntity user) {
        ArrayList<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        user.getRoles().forEach(role -> simpleGrantedAuthorities
                .add(new SimpleGrantedAuthority(role.getName().name())));

        return new UserDetailsImpl(user.getId(),
                user.getUsername(), user.getEmail(),
                user.getPassword(), simpleGrantedAuthorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
