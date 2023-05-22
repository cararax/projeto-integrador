package com.carara.my_app.rest;

import com.carara.my_app.model.CaregiverDTO;
import com.carara.my_app.service.CaregiverService;
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
@RequestMapping(value = "/api/caregivers", produces = MediaType.APPLICATION_JSON_VALUE)
public class CaregiverResource {

    private final CaregiverService caregiverService;

    public CaregiverResource(final CaregiverService caregiverService) {
        this.caregiverService = caregiverService;
    }

    @GetMapping
    public ResponseEntity<List<CaregiverDTO>> getAllCaregivers() {
        return ResponseEntity.ok(caregiverService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CaregiverDTO> getCaregiver(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(caregiverService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createCaregiver(
            @RequestBody @Valid final CaregiverDTO caregiverDTO) {
        final Long createdId = caregiverService.create(caregiverDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCaregiver(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final CaregiverDTO caregiverDTO) {
        caregiverService.update(id, caregiverDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteCaregiver(@PathVariable(name = "id") final Long id) {
        caregiverService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
