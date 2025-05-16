package SRMS.FinalProject.Service;

import SRMS.FinalProject.DTO.ApiResponse;
import SRMS.FinalProject.DTO.UserRoleResponse;
import SRMS.FinalProject.Repository.AdminRoleRepository;
import SRMS.FinalProject.Repository.AdminUserRepository;
import SRMS.FinalProject.Repository.UserRoleRepository1;
import SRMS.FinalProject.UserEntity.Role;
import SRMS.FinalProject.UserEntity.User;
import SRMS.FinalProject.UserEntity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class AdminRoleService {

    @Autowired
    private AdminRoleRepository adminRoleRepository;

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private UserRoleRepository1 userRoleRepository;

    @Transactional
    public ApiResponse<String> assignRoleToUser(Long userId, Long roleId) {
        try {
            // Fetch user and role
            User user = adminUserRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found"));

            Role role = adminRoleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Role with ID " + roleId + " not found"));

            // Check if the user already has this role assigned
            Optional<UserRole> existingUserRole = userRoleRepository.findByUserAndRole(user, role);

            if (existingUserRole.isPresent()) {
                // If role exists, delete the existing entry
                userRoleRepository.delete(existingUserRole.get());
                System.out.println("Existing role deleted for user ID: " + userId);
            }

            // After deletion (or if no existing role), create a new entry
            UserRole userRole = new UserRole();
            userRole.setUser(user);
            userRole.setRole(role);
            userRoleRepository.save(userRole);

            return new ApiResponse<>(200, "Role assigned successfully", null);
        } catch (Exception e) {
            return new ApiResponse<>(500, "Error assigning/unassigning role: " + e.getMessage(), null);
        }
    }

    @Transactional
    public ApiResponse<String> removeRoleFromUser(Long userId, Long roleId) {
        try {
            User user = adminUserRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found"));

            Role role = adminRoleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Role with ID " + roleId + " not found"));

            UserRole userRole = userRoleRepository.findByUserAndRole(user, role)
                    .orElseThrow(() -> new RuntimeException("User does not have the specified role"));

            userRoleRepository.delete(userRole);

            return new ApiResponse<>(200, "Role removed successfully", null);
        } catch (Exception e) {
            return new ApiResponse<>(500, "Error removing role: " + e.getMessage(), null);
        }
    }
    @Transactional
    public ApiResponse<String> updateUserRole(Long userId, Long roleId) {
        try {
            User user = adminUserRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found"));

            Role newRole = adminRoleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("New role with ID " + roleId + " not found"));

            // Find the current role assigned to the user
            Optional<UserRole> currentUserRole = userRoleRepository.findByUser(user);

            currentUserRole.ifPresent(userRoleRepository::delete);

            // Assign the new role
            UserRole newUserRole = new UserRole();
            newUserRole.setUser(user);
            newUserRole.setRole(newRole);
            userRoleRepository.save(newUserRole);

            return new ApiResponse<>(200, "User role updated successfully", null);
        } catch (Exception e) {
            return new ApiResponse<>(500, "Error updating user role: " + e.getMessage(), null);
        }
    }

    public Page<UserRoleResponse> getAllUserRoles(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRoleRepository.findAllUserRoles(pageable);
    }

}
