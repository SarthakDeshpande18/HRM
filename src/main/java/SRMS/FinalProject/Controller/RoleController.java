package SRMS.FinalProject.Controller;

import SRMS.FinalProject.DTO.ApiResponse;
import SRMS.FinalProject.Service.RoleService;
import SRMS.FinalProject.UserEntity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;



@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Role>> createRole(@RequestBody Role role) {
        try {
            Role createdRole = roleService.createRole(role);
            ApiResponse<Role> response = new ApiResponse<>(HttpStatus.OK.value(), "Role created successfully", createdRole);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Role> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Failed to create role: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<Role>>> getAllRoles(Pageable pageable) {
        try {
            Page<Role> rolesPage = roleService.getAllRoles(pageable);
            ApiResponse<Page<Role>> response = new ApiResponse<>(HttpStatus.OK.value(), "Roles fetched successfully", rolesPage);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Page<Role>> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Failed to fetch roles: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Role>> getRoleById(@PathVariable Long id) {
        try {
            Optional<Role> role = roleService.getRoleById(id);
            if (role.isPresent()) {
                ApiResponse<Role> response = new ApiResponse<>(HttpStatus.OK.value(), "Role fetched successfully", role.get());
                return ResponseEntity.ok(response);
            } else {
                ApiResponse<Role> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Role not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            ApiResponse<Role> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Failed to fetch role: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Role>> updateRole(@PathVariable Long id, @RequestBody Role role) {
        try {
            Role updatedRole = roleService.updateRole(id, role);
            ApiResponse<Role> response = new ApiResponse<>(HttpStatus.OK.value(), "Role updated successfully", updatedRole);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Role> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Failed to update role: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable Long id) {
        try {
            roleService.deleteRole(id);
            ApiResponse<Void> response = new ApiResponse<>(HttpStatus.OK.value(), "Role deleted successfully", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Failed to delete role: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
