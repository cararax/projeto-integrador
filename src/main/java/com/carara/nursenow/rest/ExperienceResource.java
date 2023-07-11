package com.carara.nursenow.rest;

import com.carara.nursenow.model.ExperienceDTO;
import com.carara.nursenow.model.ROLE;
import com.carara.nursenow.model.SimplePage;
import com.carara.nursenow.service.ExperienceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/experiences", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAuthority('" + ROLE.Fields.CAREGIVER + "')")
public class ExperienceResource {

    private final ExperienceService experienceService;

    public ExperienceResource(final ExperienceService experienceService) {
        this.experienceService = experienceService;
    }

    @Operation(
            parameters = {
                    @Parameter(
                            name = "page",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = Integer.class)
                    ),
                    @Parameter(
                            name = "size",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = Integer.class)
                    ),
                    @Parameter(
                            name = "sort",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = String.class)
                    )
            }
    )
    @GetMapping
    public ResponseEntity<SimplePage<ExperienceDTO>> getAllExperiences(
            @RequestParam(required = false, name = "filter") final String filter,
            @Parameter(hidden = true) @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable) {
        return ResponseEntity.ok(experienceService.findAll(filter, pageable));
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
