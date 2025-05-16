package SRMS.FinalProject.Controller;


import SRMS.FinalProject.DTO.ApiResponse;
import SRMS.FinalProject.DTO.RoleAssignmentRequest;
import SRMS.FinalProject.DTO.RoleUpdateRequest;
import SRMS.FinalProject.DTO.UserRoleResponse;
import SRMS.FinalProject.Service.AdminRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
//@PreAuthorize("hasRole('ADMIN')") // Ensures only ADMIN can access
public class AdminRoleController {

    @Autowired
    private AdminRoleService adminRoleService;


    @PostMapping("/assign-role")
    public ResponseEntity<ApiResponse<String>> assignRoleToUser(@RequestBody RoleAssignmentRequest request) {
        try {
            // Log to verify request body
            System.out.println("Assigning role with ID: " + request.getRoleId() + " to User with ID: " + request.getUserId());

            adminRoleService.assignRoleToUser(request.getUserId(), request.getRoleId());
            return ResponseEntity.ok(new ApiResponse<>(200, "Role assigned to user successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "Error assigning role: " + e.getMessage(), null));
        }
    }
    
    @PutMapping("/update-role")
    public ResponseEntity<ApiResponse<String>> updateUserRole(@RequestBody RoleUpdateRequest request) {
        try {
            adminRoleService.updateUserRole(request.getUserId(), request.getRoleId());
            return ResponseEntity.ok(new ApiResponse<>(200, "User role updated successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "Error updating role: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/remove-role")
    public ResponseEntity<ApiResponse<String>> removeRoleFromUser(
            @RequestParam Long userId,
            @RequestParam Long roleId) {
        try {
            adminRoleService.removeRoleFromUser(userId, roleId);
            return ResponseEntity.ok(new ApiResponse<>(200, "Role removed from user successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "Error removing role: " + e.getMessage(), null));
        }
    }

    @GetMapping("/user-roles")
    public ResponseEntity<ApiResponse<Page<UserRoleResponse>>> getAllUserRoles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<UserRoleResponse> userRolesPage = adminRoleService.getAllUserRoles(page, size);
            return ResponseEntity.ok(new ApiResponse<>(200, "User roles fetched successfully", userRolesPage));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "Error fetching user roles: " + e.getMessage(), null));
        }
    }


}