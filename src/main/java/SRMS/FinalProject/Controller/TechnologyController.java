package SRMS.FinalProject.Controller;

import SRMS.FinalProject.DTO.ApiResponse;
import SRMS.FinalProject.Service.TechnologyService;
import SRMS.FinalProject.UserEntity.Technology;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
//
//@RestController
//@RequestMapping("/api/technologies")
//public class TechnologyController {
//
//    @Autowired
//    private TechnologyService technologyService;
//
//    @PostMapping
//    public ResponseEntity<ApiResponse<Technology>> createTechnology(@RequestBody Technology technology) {
//        Technology savedTech = technologyService.addTechnology(technology);
//        if (savedTech == null) {
//            return ResponseEntity.status(HttpStatus.CONFLICT)
//                    .body(new ApiResponse<>(409, "Technology already exists", null));
//        }
//        return ResponseEntity.ok(new ApiResponse<>(200, "Technology created successfully", savedTech));
//    }
//
//
//    @GetMapping
//    public ResponseEntity<ApiResponse<List<Technology>>> getAllTechnologies() {
//        List<Technology> techList = technologyService.getAllTechnologies();
//        return ResponseEntity.ok(new ApiResponse<>(200, "Fetched all technologies", techList));
//    }
//
//    @PostMapping("/getById")
//    public ResponseEntity<ApiResponse<Technology>> getTechnologyById(@RequestBody Map<String, Long> payload) {
//        Long id = payload.get("tech_id");
//        return technologyService.getTechnologyById(id)
//                .map(tech -> ResponseEntity.ok(new ApiResponse<>(200, "Successfull", tech)))
//                .orElse(ResponseEntity.status(404).body(new ApiResponse<>(404, "Technology ID " + id + " not found", null)));
//    }
//
//
//    @PostMapping("/update")
//    public ResponseEntity<ApiResponse<Technology>> updateTechnology(@RequestBody Technology technology) {
//        Long id = technology.getTech_id();
//        try {
//            Technology updatedTech = technologyService.updateTechnology(id, technology);
//            return ResponseEntity.ok(new ApiResponse<>(200, "Technology updated successfully", updatedTech));
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(404).body(new ApiResponse<>(404, "Technology ID " + id + " not found", null));
//        }
//    }
//
//    @PostMapping("/delete")
//    public ResponseEntity<ApiResponse<Void>> deleteTechnology(@RequestBody Map<String, Long> payload) {
//        Long id = payload.get("tech_id");
//        boolean deleted = technologyService.deleteTechnology(id);
//        if (deleted) {
//            return ResponseEntity.ok(new ApiResponse<>(200, "Technology deleted successfully", null));
//        } else {
//            return ResponseEntity.status(404).body(new ApiResponse<>(404, "Technology ID " + id + " not found", null));
//        }
//    }
//
//}


@RestController
@RequestMapping("/api/technologies")
public class TechnologyController {

    @Autowired
    private TechnologyService technologyService;

    // Create a new Technology
    @PostMapping
    public ResponseEntity<ApiResponse<Technology>> createTechnology(@RequestBody Technology technology) {
        Technology savedTech = technologyService.addTechnology(technology);
        if (savedTech == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(409, "Technology already exists", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(200, "Technology created successfully", savedTech));
    }

    // Get all Technologies with pagination
    @GetMapping
    public ResponseEntity<ApiResponse<Page<Technology>>> getAllTechnologies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Technology> techPage = technologyService.getAllTechnologies(pageable);
        return ResponseEntity.ok(new ApiResponse<>(200, "Fetched all technologies", techPage));
    }

    // Get Technology by ID
    @PostMapping("/getById")
    public ResponseEntity<ApiResponse<Technology>> getTechnologyById(@RequestBody Map<String, Long> payload) {
        Long id = payload.get("tech_id");
        return technologyService.getTechnologyById(id)
                .map(tech -> ResponseEntity.ok(new ApiResponse<>(200, "Successfull", tech)))
                .orElse(ResponseEntity.status(404).body(new ApiResponse<>(404, "Technology ID " + id + " not found", null)));
    }

    // Update Technology
    @PostMapping("/update")
    public ResponseEntity<ApiResponse<Technology>> updateTechnology(@RequestBody Technology technology) {
        Long id = technology.getTech_id();
        try {
            Technology updatedTech = technologyService.updateTechnology(id, technology);
            return ResponseEntity.ok(new ApiResponse<>(200, "Technology updated successfully", updatedTech));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new ApiResponse<>(404, "Technology ID " + id + " not found", null));
        }
    }

    // Delete Technology
    @PostMapping("/delete")
    public ResponseEntity<ApiResponse<Void>> deleteTechnology(@RequestBody Map<String, Long> payload) {
        Long id = payload.get("tech_id");
        boolean deleted = technologyService.deleteTechnology(id);
        if (deleted) {
            return ResponseEntity.ok(new ApiResponse<>(200, "Technology deleted successfully", null));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse<>(404, "Technology ID " + id + " not found", null));
        }
    }

}
