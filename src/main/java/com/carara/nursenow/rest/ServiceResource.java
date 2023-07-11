package com.carara.nursenow.rest;

import com.carara.nursenow.model.ROLE;
import com.carara.nursenow.model.ServiceDTO;
import com.carara.nursenow.model.SimplePage;
import com.carara.nursenow.service.ServiceService;
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
@RequestMapping(value = "/api/services", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAuthority('" + ROLE.Fields.CAREGIVER + "')")
public class ServiceResource {

    private final ServiceService serviceService;

    public ServiceResource(final ServiceService serviceService) {
        this.serviceService = serviceService;
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
    public ResponseEntity<SimplePage<ServiceDTO>> getAllServices(
            @RequestParam(required = false, name = "filter") final String filter,
            @Parameter(hidden = true) @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable) {
        return ResponseEntity.ok(serviceService.findAll(filter, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceDTO> getService(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(serviceService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createService(@RequestBody @Valid final ServiceDTO serviceDTO) {
        final Long createdId = serviceService.create(serviceDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateService(@PathVariable(name = "id") final Long id,
                                              @RequestBody @Valid final ServiceDTO serviceDTO) {
        serviceService.update(id, serviceDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteService(@PathVariable(name = "id") final Long id) {
        serviceService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
