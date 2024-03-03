package com.example.BankingOperationsService.controller;

import com.example.BankingOperationsService.dto.TransferDto;
import com.example.BankingOperationsService.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/transfers")
@AllArgsConstructor
@Tag(name = "Transfers", description = "Endpoints for money transfers")
public class TransferController {
    private final TransferService transferService;

    @Operation(summary = "Transfer money", description = "Transfers money with the provided transfer details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully transferred money",
                    content = @Content(schema = @Schema(implementation = TransferDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping
    public ResponseEntity<?> transferMoney(@RequestBody TransferDto transferDto) {
        try {
            return transferService.transferMoney(transferDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Произошла ошибка при переводе средств");
        }
    }
}
