package SRMS.FinalProject.Repository;


import SRMS.FinalProject.DTO.UserRoleResponse;
import SRMS.FinalProject.UserEntity.Role;
import SRMS.FinalProject.UserEntity.User;
import SRMS.FinalProject.UserEntity.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleRepository1 extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByUser(User user);  // Fetch the existing role of the user
    Optional<UserRole> findByUserAndRole(User user, Role role);
    boolean existsByUserAndRole(User user, Role role);


    @Query("SELECT new SRMS.FinalProject.DTO.UserRoleResponse(ur.id, ur.user.id, ur.user.username, ur.role.id, ur.role.roleName) FROM UserRole ur")
    Page<UserRoleResponse> findAllUserRoles(Pageable pageable);

}


