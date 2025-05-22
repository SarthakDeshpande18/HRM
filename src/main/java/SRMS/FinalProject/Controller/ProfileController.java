package SRMS.FinalProject.Controller;

import SRMS.FinalProject.Config.JwtTokenProvider;
import SRMS.FinalProject.DTO.ApiResponse;
import SRMS.FinalProject.DTO.ProfileDTO;
import SRMS.FinalProject.Service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider; // To extract userId from token

    // 1. For JSON update (no file)
    
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<String>> updateProfileJson(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody ProfileDTO profileDTO) {

        return updateProfileInternal(authHeader, profileDTO, null);
    }

    // 2. For multipart update (with optional file)
    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> updateProfileMultipart(
            @RequestHeader("Authorization") String authHeader,
            @RequestPart(value = "profileDTO", required = false) ProfileDTO profileDTO,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {

        return updateProfileInternal(authHeader, profileDTO, profileImage);
    }

    // Internal common logic to avoid duplication
    private ResponseEntity<ApiResponse<String>> updateProfileInternal(
            String authHeader, ProfileDTO profileDTO, MultipartFile profileImage) {

        if (profileDTO == null) {
            ApiResponse<String> response = new ApiResponse<>(400, "Profile data is required", null);
            return ResponseEntity.badRequest().body(response);
        }

        if (profileImage != null && !profileImage.isEmpty()) {
            String originalFilename = profileImage.getOriginalFilename();
            if (originalFilename == null ||
                    !(originalFilename.toLowerCase().endsWith(".jpg") || originalFilename.toLowerCase().endsWith(".png"))) {
                ApiResponse<String> response = new ApiResponse<>(400, "Only .jpg or .png files are allowed for profile picture", null);
                return ResponseEntity.badRequest().body(response);
            }

        }

        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;

        if (token.trim().isEmpty()) {
            ApiResponse<String> response = new ApiResponse<>(401, "Token is missing or invalid", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        Long userId;
        try {
            userId = jwtTokenProvider.getUserIdFromToken(token);
        } catch (Exception e) {
            ApiResponse<String> response = new ApiResponse<>(401, "Invalid or expired token", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        ApiResponse<String> response = profileService.addProfileFields(userId, profileDTO, profileImage);

        return switch (response.getStatus()) {
            case 200 -> ResponseEntity.ok(response);
            case 400 -> ResponseEntity.badRequest().body(response);
            case 500 -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            default -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        };
    }
}