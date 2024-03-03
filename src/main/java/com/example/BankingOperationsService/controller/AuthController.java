package com.example.BankingOperationsService.controller;

import com.example.BankingOperationsService.dto.LoginDto;
import com.example.BankingOperationsService.dto.RegistrationDto;
import com.example.BankingOperationsService.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Register a new user", description = "Registers a new user with the provided user data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully registered user",
                    content = @Content(schema = @Schema(implementation = RegistrationDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationDto registrationDto) {
        return authService.register(registrationDto.toUser());
    }

    @Operation(summary = "Authenticate user", description = "Authenticates a user with the provided login credentials.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully authenticated user",
                    content = @Content(schema = @Schema(implementation = LoginDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDto loginDto) {
        return authService.authenticate(loginDto);
    }
}