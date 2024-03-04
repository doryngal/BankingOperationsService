package com.example.BankingOperationsService.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/search")
@Tag(name = "User Search", description = "User Search Controller")
public class SearchController {
    private final UserService userService;


    @Operation(summary = "Find users by birth date after the specified date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found users",
                    content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "404", description = "Users not found")
    })
    @GetMapping("/search/byBirthDateAfter")
    public ResponseEntity<List<UserDto>> findByBirthDateAfter(@RequestParam("date") LocalDate date) {
        return ResponseEntity.ok(userService.findByBirthDateAfter(date));
    }

    @Operation(summary = "Find users by phone")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found users",
                    content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "404", description = "Users not found")
    })
    @GetMapping("/search/byPhone")
    public ResponseEntity<UserDto> findByPhone(@RequestParam("phone") String phone) {
        return ResponseEntity.ok(userService.findByPhone(phone));
    }

    @Operation(summary = "Find users by full name starting with the specified prefix")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found users",
                    content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "404", description = "Users not found")
    })
    @GetMapping("/search/byFullName")
    public ResponseEntity<List<UserDto>> findByFullName(@RequestParam("fullName") String fullName) {
        return ResponseEntity.ok(userService.findByFullName(fullName));
    }

    @Operation(summary = "Find users by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found users",
                    content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "404", description = "Users not found")
    })
    @GetMapping("/search/byEmail")
    public ResponseEntity<UserDto> findByEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }
}
