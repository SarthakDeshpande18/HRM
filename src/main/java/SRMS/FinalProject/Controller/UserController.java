package SRMS.FinalProject.Controller;


import SRMS.FinalProject.DTO.*;
import SRMS.FinalProject.JwtUtil.JwtUtil;
import SRMS.FinalProject.Service.UserService;
import SRMS.FinalProject.UserEntity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/auth/Users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
 private JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<ApiResponse<Page<UserDto>>> getUsers(
            @RequestBody UserFilterRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (request.getFilter() == null) {
            ApiResponse<Page<UserDto>> response = new ApiResponse<>(400, "Filter value is required", null);
            return ResponseEntity.badRequest().body(response);
        }

        int filter = request.getFilter();
        String filterType;

        switch (filter) {
            case 0:
                filterType = "all";
                break;
            case 1:
                filterType = "verified";
                break;
            case 2:
                filterType = "unverified";
                break;
            default:
                return ResponseEntity.badRequest().body(
                        new ApiResponse<>(400, "Invalid filter value.", null)
                );
        }

        Page<UserDto> users = userService.getAllUsers(filterType, page, size);
        return ResponseEntity.ok(new ApiResponse<>(200, "Data fetched successfully", users));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDto>> getCurrentUser(HttpServletRequest request) {
        try {
            String token = jwtUtil.extractToken(request);

            Long userId = jwtUtil.extractUserId(token);

            UserDto userDto = userService.getUserById(userId);
            if (userDto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(404, "User not found", null));
            }

            return ResponseEntity.ok(new ApiResponse<>(200, "User fetched successfully", userDto));

        } catch (Exception e) {
            e.printStackTrace(); // Log the exact error
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(403, "Invalid or expired token", null));
        }
    }


    @PostMapping("/getById")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@RequestBody IdRequest request) {
        Long id = request.getId();
        if (id == null) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "ID is required", null));
        }

        UserDto userDto = userService.getUserById(id);
        if (userDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(404, "User with id " + id + " not found", null));
        }

        return ResponseEntity.ok(new ApiResponse<>(200, "User fetched successfully", userDto));
    }

}

