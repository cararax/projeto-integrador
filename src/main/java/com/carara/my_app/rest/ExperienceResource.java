package com.carara.my_app.rest;

import com.carara.my_app.model.ExperienceDTO;
import com.carara.my_app.service.ExperienceService;
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
@RequestMapping(value = "/api/experiences", produces = MediaType.APPLICATION_JSON_VALUE)
public class ExperienceResource {

    private final ExperienceService experienceService;

    public ExperienceResource(final ExperienceService experienceService) {
        this.experienceService = experienceService;
    }

    @GetMapping
    public ResponseEntity<List<ExperienceDTO>> getAllExperiences() {
        return ResponseEntity.ok(experienceService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExperienceDTO> getExperience(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(experienceService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createExperience(
            @RequestBody @Valid final ExperienceDTO experienceDTO) {
        final Long createdId = experienceService.create(experienceDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateExperience(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final ExperienceDTO experienceDTO) {
        experienceService.update(id, experienceDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteExperience(@PathVariable(name = "id") final Long id) {
        experienceService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
