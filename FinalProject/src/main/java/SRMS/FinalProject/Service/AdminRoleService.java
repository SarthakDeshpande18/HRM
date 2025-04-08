package SRMS.FinalProject.Service;

import SRMS.FinalProject.Repository.AdminRoleRepository;
import SRMS.FinalProject.Repository.AdminUserRepository;
import SRMS.FinalProject.Repository.UserRoleRepository1;
import SRMS.FinalProject.UserEntity.Role;
import SRMS.FinalProject.UserEntity.User;
import SRMS.FinalProject.UserEntity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AdminRoleService {

    @Autowired
    private AdminRoleRepository adminRoleRepository;

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private UserRoleRepository1 userRoleRepository;

    public boolean assignRoleToUser(Long userId, Long roleId) {
        try {
            User user = adminUserRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found"));

            Role role = adminRoleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Role with ID " + roleId + " not found"));

            Optional<UserRole> existingUserRole = userRoleRepository.findByUserAndRole(user, role);

            if (existingUserRole.isPresent()) {
                userRoleRepository.delete(existingUserRole.get());
                // Return false to indicate it was unassigned
                return false;
            }

            UserRole userRole = new UserRole();
            userRole.setUser(user);
            userRole.setRole(role);
            userRoleRepository.save(userRole);

            // Return true to indicate it was assigned
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error assigning/unassigning role: " + e.getMessage(), e);
        }
    }


    public void removeRoleFromUser(Long userId, Long roleId) {
        try {
            User user = adminUserRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found"));

            Role role = adminRoleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Role with ID " + roleId + " not found"));

            UserRole userRole = userRoleRepository.findByUserAndRole(user, role)
                    .orElseThrow(() -> new RuntimeException("User does not have the specified role"));

            userRoleRepository.delete(userRole);
        } catch (Exception e) {
            throw new RuntimeException("Error removing role: " + e.getMessage(), e);
        }
    }

    public void updateUserRole(Long userId, Long roleId) {
        try {
            User user = adminUserRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found"));

            Role newRole = adminRoleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("New role with ID " + roleId + " not found"));

            // Find the current role assigned to the user
            Optional<UserRole> currentUserRole = userRoleRepository.findByUser(user);

            if (currentUserRole.isPresent()) {
                // Remove the old role
                userRoleRepository.delete(currentUserRole.get());
            }

            // Assign the new role
            UserRole newUserRole = new UserRole();
            newUserRole.setUser(user);
            newUserRole.setRole(newRole);

            userRoleRepository.save(newUserRole);
        } catch (Exception e) {
            throw new RuntimeException("Error updating user role: " + e.getMessage(), e);
        }
    }
}
