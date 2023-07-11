package com.carara.nursenow.rest;

import com.carara.nursenow.model.CityDTO;
import com.carara.nursenow.model.ROLE;
import com.carara.nursenow.model.SimplePage;
import com.carara.nursenow.service.CityService;
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
@RequestMapping(value = "/api/citys", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAuthority('" + ROLE.Fields.CAREGIVER + "')")
public class CityResource {

    private final CityService cityService;

    public CityResource(final CityService cityService) {
        this.cityService = cityService;
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
    public ResponseEntity<SimplePage<CityDTO>> getAllCitys(
            @RequestParam(required = false, name = "filter") final String filter,
            @Parameter(hidden = true) @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable) {
        return ResponseEntity.ok(cityService.findAll(filter, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityDTO> getCity(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(cityService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createCity(@RequestBody @Valid final CityDTO cityDTO) {
        final Long createdId = cityService.create(cityDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCity(@PathVariable(name = "id") final Long id,
                                           @RequestBody @Valid final CityDTO cityDTO) {
        cityService.update(id, cityDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteCity(@PathVariable(name = "id") final Long id) {
        cityService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
