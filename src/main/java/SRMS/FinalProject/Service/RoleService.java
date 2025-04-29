package SRMS.FinalProject.Service;


import SRMS.FinalProject.Repository.RoleRepository;
import SRMS.FinalProject.UserEntity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

//        public Page<Role> getAllRoles(Pageable pageable) {
//            return roleRepository.findAll(pageable);
//        }

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

//
//@Service
//public class RoleService {
//
//    private final RoleRepository roleRepository;
//
//    public RoleService(RoleRepository roleRepository) {
//        this.roleRepository = roleRepository;
//    }
//
//    public Role createRole(Role role) {
//        Optional<Role> existingRole = roleRepository.findByRoleName(role.getRoleName());
//        if (existingRole.isPresent()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role with name '" + role.getRoleName() + "' already exists.");
//        }
//        return roleRepository.save(role);
//    }
//
//    public Page<Role> getAllRoles(int page, int size, String sortBy, String sortDir) {
//        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
//        Pageable pageable = PageRequest.of(page, size, sort);
//        return roleRepository.findAll(pageable);
//    }
//
//    public Optional<Role> getRoleById(Long id) {
//        return roleRepository.findById(id);
//    }
//
//    public Role updateRole(Long id, Role roleDetails) {
//        Role role = roleRepository.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found with id: " + id));
//        role.setRoleName(roleDetails.getRoleName());
//        role.setDescription(roleDetails.getDescription());
//        return roleRepository.save(role);
//    }
//
//    public void deleteRole(Long id) {
//        Role role = roleRepository.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found with id: " + id));
//        roleRepository.delete(role);
//    }
//}
