package com.example.BankingOperationsService.controller;

import com.example.BankingOperationsService.dto.ContactDto;
import com.example.BankingOperationsService.dto.MessageDto;
import com.example.BankingOperationsService.dto.SearchDto;
import com.example.BankingOperationsService.dto.UserDto;
import com.example.BankingOperationsService.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/user")
@AllArgsConstructor
@Tag(name = "User Management", description = "Endpoints for managing user data")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Get user by ID", description = "Retrieve user details by user ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user details",
                    content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        try {
            UserDto userDto = userService.getUserById(userId);
            return ResponseEntity.ok(userDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Add contact", description = "Add a new contact for the user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added contact"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/add-contact")
    public ResponseEntity<?> addContact(@RequestBody ContactDto contactDto) {
        try {
            userService.addContact(contactDto);
            return ResponseEntity.ok().body("Контакт успешно добавлен.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Update contact", description = "Update an existing contact for the user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated contact"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PutMapping("/edit-contact/{contactId}")
    public ResponseEntity<?> updateContact(@PathVariable Long contactId, @RequestBody ContactDto contactDto) {
        try {
            return userService.updateContact(contactId, contactDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Delete user contact", description = "Delete a user's contact by contact ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted user contact"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @DeleteMapping("/delete-contact/{contactId}")
    public ResponseEntity<?> deleteUserContact(@PathVariable Long contactId) {
        try {
            return ResponseEntity.ok(userService.deleteUserContact(contactId));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
