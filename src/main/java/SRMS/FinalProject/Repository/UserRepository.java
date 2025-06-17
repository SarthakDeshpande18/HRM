
package SRMS.FinalProject.Repository;


import SRMS.FinalProject.UserEntity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByEmailVerified(Boolean emailVerified);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByMobileNo(String mobileNo);
    Optional<User> findBymobileNo(String mobileNo);
    Page<User> findByEmailVerified(boolean emailVerified, Pageable pageable);

    @Query("SELECT u FROM User u " +
            "JOIN UserRole ur ON u.id = ur.user.id " +
            "JOIN UserTechnology ut ON u.id = ut.user.id " +
            "WHERE ur.role.roleName = 'INTERVIEWER' AND ut.technology.id = :technologyId")
    List<User> findInterviewersByTechnology(@Param("technologyId") Long technologyId);

    User findByFirstNameAndLastName(String firstName, String lastName);
}




