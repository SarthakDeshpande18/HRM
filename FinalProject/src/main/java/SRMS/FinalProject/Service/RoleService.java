package SRMS.FinalProject.Service;


import SRMS.FinalProject.Repository.RoleRepository;
import SRMS.FinalProject.UserEntity.Role;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role createRole(Role role) {
        Optional<Role> existingRole = roleRepository.findByRoleName(role.getRoleName());
        if (existingRole.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role with name '" + role.getRoleName() + "' already exists.");
        }
        return roleRepository.save(role);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    public Role updateRole(Long id, Role roleDetails) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found with id: " + id));
        role.setRoleName(roleDetails.getRoleName());
        role.setDescription(roleDetails.getDescription());
        return roleRepository.save(role);
    }

    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found with id: " + id));
        roleRepository.delete(role);
    }
}
