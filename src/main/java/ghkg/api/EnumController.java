package ghkg.api;


import ghkg.config.ApiPaths;
import ghkg.domain.car.FuelType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@Tag(name = "Enums", description = "Enum values")
@RestController
@RequestMapping(ApiPaths.ENUMS)
public class EnumController {

    @Operation(summary = "List of all available FuelType values", tags = {"Enums"})
    @GetMapping("/fuel-types")
    public ResponseEntity<List<String>> getFuelTypes() {
        List<String> fuelTypes = Arrays.stream(FuelType.values())
                .map(Enum::name)
                .toList();
        return ResponseEntity.ok(fuelTypes);
    }
}
