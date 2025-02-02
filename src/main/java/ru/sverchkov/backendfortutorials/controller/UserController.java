package ru.sverchkov.backendfortutorials.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.sverchkov.backendfortutorials.exception.EmptyTokenException;
import ru.sverchkov.backendfortutorials.exception.UserNotFoundException;
import ru.sverchkov.backendfortutorials.model.request.UserUpdateRequest;
import ru.sverchkov.backendfortutorials.model.response.EmptyTokenResponse;
import ru.sverchkov.backendfortutorials.model.response.MessageResponse;
import ru.sverchkov.backendfortutorials.model.response.UserResponse;
import ru.sverchkov.backendfortutorials.service.UserService;

import java.util.Date;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User", description = "Operations with user")
@CrossOrigin(origins = "*", maxAge = 4800, methods = {RequestMethod.GET})
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'MODERATOR', 'ADMIN')")
    public MessageResponse userAccess() {
        return new MessageResponse("Congratulations! You are an authenticated user.");
    }

    @GetMapping("/mod")
    @PreAuthorize("hasAnyRole('MODERATOR', 'ADMIN')")
    public MessageResponse moderatorAccess() {
        return new MessageResponse("Moderator Board.");
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public MessageResponse adminAccess() {
        return new MessageResponse("Admin Board.");
    }


    @PutMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class))
            })
    })
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateRequest request, @PathVariable(name = "id") String id) {
        return ResponseEntity.ok(userService.updateUser(request, id));
    }

    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class))
            })
    })
    public ResponseEntity<?> getUser(@PathVariable(name = "id") String id) {
        return ResponseEntity.ok(userService.loadUserById(id));
    }

    @Operation(summary = "Get all users", tags = "user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users founds", content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserResponse.class)))
            })
    })
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }


    @ExceptionHandler(EmptyTokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<EmptyTokenResponse> handleEmptyTokenException(EmptyTokenException exception) {
        return ResponseEntity.badRequest().body(EmptyTokenResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .timestamp(new Date())
                .build());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<UserNotFoundException> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.badRequest().body(e);
    }
}
