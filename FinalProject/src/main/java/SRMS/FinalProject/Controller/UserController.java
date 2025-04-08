package SRMS.FinalProject.Controller;


import SRMS.FinalProject.DTO.ApiResponse;
import SRMS.FinalProject.DTO.UserBasicDto;
import SRMS.FinalProject.DTO.UserDto;
import SRMS.FinalProject.DTO.UserFilterRequest;
import SRMS.FinalProject.Service.UserService;
import SRMS.FinalProject.UserEntity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/Users")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping
    public ResponseEntity<ApiResponse<List<UserDto>>> getUsers(@RequestBody UserFilterRequest request) {
        if (request.getFilter() == null) {
            ApiResponse<List<UserDto>> response = new ApiResponse<>(400, "Filter value is required", null);
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

        List<UserDto> users = userService.getAllUsers(filterType);
        return ResponseEntity.ok(new ApiResponse<>(200, "Data fetched successfully", users));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserBasicDto>> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(404, "User not found", null));
        }

        UserBasicDto userDto = new UserBasicDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getEmailVerified()
        );

        return ResponseEntity.ok(new ApiResponse<>(200, "User fetched successfully", userDto));
    }
}
