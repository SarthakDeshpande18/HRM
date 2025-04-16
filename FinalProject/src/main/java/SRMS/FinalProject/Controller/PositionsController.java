package SRMS.FinalProject.Controller;

import SRMS.FinalProject.DTO.ApiResponse;
import SRMS.FinalProject.Service.PositionsService;
import SRMS.FinalProject.UserEntity.Positions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/positions")
public class PositionsController {

    @Autowired
    private PositionsService positionsService;

    @PostMapping
    public ResponseEntity<ApiResponse<Positions>> createPosition(@RequestBody Positions position) {
        Positions savedPosition = positionsService.addPosition(position);
        return ResponseEntity.ok(new ApiResponse<>(200, "Position created successfully", savedPosition));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Positions>>> getAllPositions() {
        List<Positions> positionsList = positionsService.getAllPositions();
        return ResponseEntity.ok(new ApiResponse<>(200, "Fetched all positions", positionsList));
    }

    @PostMapping("/getById")
    public ResponseEntity<ApiResponse<Positions>> getPositionById(@RequestBody Map<String, Long> payload) {
        Long id = payload.get("id");
        return positionsService.getPositionById(id)
                .map(position -> ResponseEntity.ok(new ApiResponse<>(200, "Successful", position)))
                .orElse(ResponseEntity.status(404).body(new ApiResponse<>(404, "Position ID " + id + " not found", null)));
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<Positions>> updatePosition(@RequestBody Positions position) {
        Long id = position.getId();
        try {
            Positions updatedPosition = positionsService.updatePosition(id, position);
            return ResponseEntity.ok(new ApiResponse<>(200, "Position updated successfully", updatedPosition));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new ApiResponse<>(404, "Position ID " + id + " not found", null));
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<ApiResponse<Void>> deletePosition(@RequestBody Map<String, Long> payload) {
        Long id = payload.get("id");
        boolean deleted = positionsService.deletePosition(id);
        if (deleted) {
            return ResponseEntity.ok(new ApiResponse<>(200, "Position deleted successfully", null));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse<>(404, "Position ID " + id + " not found", null));
        }
    }
}