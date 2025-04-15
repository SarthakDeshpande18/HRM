package SRMS.FinalProject.Controller;

import SRMS.FinalProject.DTO.ApiResponse;
import SRMS.FinalProject.DTO.ProfileDTO;
import SRMS.FinalProject.Service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<String>> addProfileFields(
            @PathVariable Long id,
            @RequestBody(required = false) ProfileDTO profileDTO) {

        if (profileDTO == null) {
            ApiResponse<String> response = new ApiResponse<>(400, "Profile data is required", null);
            return ResponseEntity.badRequest().body(response);
        }

        ApiResponse<String> response = profileService.addProfileFields(id, profileDTO);

        if (response.getStatus() == 200) {
            return ResponseEntity.ok(response);
        } else if (response.getStatus() == 400) {
            return ResponseEntity.badRequest().body(response);
        } else {
            return ResponseEntity.status(500).body(response);
        }
    }
}
