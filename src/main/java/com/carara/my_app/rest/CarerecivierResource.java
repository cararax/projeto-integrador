package com.carara.my_app.rest;

import com.carara.my_app.model.CarerecivierDTO;
import com.carara.my_app.service.CarerecivierService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/carereciviers", produces = MediaType.APPLICATION_JSON_VALUE)
public class CarerecivierResource {

    private final CarerecivierService carerecivierService;

    public CarerecivierResource(final CarerecivierService carerecivierService) {
        this.carerecivierService = carerecivierService;
    }

    @GetMapping
    public ResponseEntity<List<CarerecivierDTO>> getAllCarereciviers() {
        return ResponseEntity.ok(carerecivierService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarerecivierDTO> getCarerecivier(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(carerecivierService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createCarerecivier(
            @RequestBody @Valid final CarerecivierDTO carerecivierDTO) {
        final Long createdId = carerecivierService.create(carerecivierDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCarerecivier(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final CarerecivierDTO carerecivierDTO) {
        carerecivierService.update(id, carerecivierDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteCarerecivier(@PathVariable(name = "id") final Long id) {
        carerecivierService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
