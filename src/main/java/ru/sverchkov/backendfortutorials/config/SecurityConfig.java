package ru.sverchkov.backendfortutorials.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ru.sverchkov.backendfortutorials.jwt.AuthEntryPointJwt;
import ru.sverchkov.backendfortutorials.jwt.AuthTokenFilter;
import ru.sverchkov.backendfortutorials.jwt.JwtUtils;
import ru.sverchkov.backendfortutorials.model.dto.ERole;
import ru.sverchkov.backendfortutorials.model.entity.RoleEntity;
import ru.sverchkov.backendfortutorials.repository.RoleRepository;
import ru.sverchkov.backendfortutorials.service.impl.UserDetailsServiceImpl;

import java.util.List;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private static final String[] ANONYMOUS_LIST_URLS = {
            "/api/auth/**",
            "/api/tutorials/published",
            "/api/tutorials/page/**",
            "/api/feedback/active",
            "/api/feedback/bytutorial/**",
            "/api/swagger.html",
            "/api/swagger-ui/**",
            "/v3/api-docs/**"
    };

    private static final String[] PERMIT_ALL_LIST_URLS = {
            "/api/user/**"
    };

    private static final String[] USER_LIST_URLS = {
            "/api/tutorials/**",
            "/api/feedback/byuser/**",
            "/api/feedback/all",
            "/api/user/user"
    };

    private static final String[] MODERATOR_LIST_URLS = {
            "/api/user/mod"
    };

    private static final String[] ADMIN_LIST_URLS = {
            "/api/user/all",
            "/api/user/admin"
    };

    private final AuthEntryPointJwt unauthorizedHandler;

    private final UserDetailsServiceImpl userDetailsService;

    private final JwtUtils jwtUtils;

    public SecurityConfig(AuthEntryPointJwt unauthorizedHandler, UserDetailsServiceImpl userDetailsService, JwtUtils jwtUtils) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .httpBasic(c -> c.authenticationEntryPoint(unauthorizedHandler))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(ANONYMOUS_LIST_URLS).anonymous()
                        .requestMatchers(USER_LIST_URLS).hasAnyAuthority("ADMIN", "MODERATOR", "USER")
                        .requestMatchers(MODERATOR_LIST_URLS).hasAnyAuthority("ADMIN", "MODERATOR")
                        .requestMatchers(ADMIN_LIST_URLS).hasAnyAuthority("ADMIN")
                        .requestMatchers(PERMIT_ALL_LIST_URLS).permitAll()
                        .anyRequest().authenticated())
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .permitAll());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter(userDetailsService, jwtUtils);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration
                .getAuthenticationManager();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public List<RoleEntity> initDatabase(RoleRepository roleRepository) {
        RoleEntity buildAdmin = null, buildUser = null, buildModerator = null;
        if (roleRepository.findByName(ERole.USER).isEmpty()) {
            buildUser = RoleEntity.builder()
                    .name(ERole.USER)
                    .build();
        }

        if (roleRepository.findByName(ERole.MODERATOR).isEmpty()) {
            buildModerator = RoleEntity.builder()
                    .name(ERole.MODERATOR)
                    .build();
        }

        if (roleRepository.findByName(ERole.ADMIN).isEmpty()) {
            buildAdmin = RoleEntity.builder()
                    .name(ERole.ADMIN)
                    .build();
        }
        if (buildUser != null || buildModerator != null || buildAdmin != null) {
            return roleRepository.saveAll(List.of(buildUser, buildModerator, buildAdmin));
        } else {
            return null;
        }
    }
}
