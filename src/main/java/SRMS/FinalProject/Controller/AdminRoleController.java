package SRMS.FinalProject.Controller;


import SRMS.FinalProject.DTO.ApiResponse;
import SRMS.FinalProject.DTO.RoleAssignmentRequest;
import SRMS.FinalProject.DTO.RoleUpdateRequest;
import SRMS.FinalProject.Service.AdminRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')") // Ensures only ADMIN can access
public class AdminRoleController {

    @Autowired
    private AdminRoleService adminRoleService;


    @PostMapping("/assign-role")
    public ResponseEntity<ApiResponse<String>> assignRoleToUser(@RequestBody RoleAssignmentRequest request) {
        try {
            boolean assigned = adminRoleService.assignRoleToUser(request.getUserId(), request.getRoleId());
            if (assigned) {
                return ResponseEntity.ok(new ApiResponse<>(200, "Role assigned successfully", null));
            } else {
                return ResponseEntity.ok(new ApiResponse<>(200, "Role unassigned successfully", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, " " + e.getMessage(), null));
        }
    }

    @PutMapping("/update-role")
    public ResponseEntity<ApiResponse<String>> updateUserRole(@RequestBody RoleUpdateRequest request) {
        try {
            adminRoleService.updateUserRole(request.getUserId(), request.getRoleId());
            return ResponseEntity.ok(new ApiResponse<>(200, "User role updated successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, " " + e.getMessage(), null));
        }
    }
}

