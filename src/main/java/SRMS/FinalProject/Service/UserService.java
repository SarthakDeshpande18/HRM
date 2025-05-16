package SRMS.FinalProject.Service;


import SRMS.FinalProject.DTO.UserDto;
import SRMS.FinalProject.Repository.UserRepository;
import SRMS.FinalProject.UserEntity.Role;
import SRMS.FinalProject.UserEntity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//
//@Service
//public class UserService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    public Page<UserDto> getAllUsers(String filterType, int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        Page<User> usersPage;
//
//        switch (filterType) {
//            case "verified":
//                usersPage = userRepository.findByEmailVerified(true, pageable);
//                break;
//            case "unverified":
//                usersPage = userRepository.findByEmailVerified(false, pageable);
//                break;
//            case "all":
//            default:
//                usersPage = userRepository.findAll(pageable);
//                break;
//        }
//
//        return usersPage.map(user -> new UserDto(
//                user.getId(),
//                user.getEmail(),
//                user.getUsername(),
//                user.getMobileNo(),
//                user.getEmailVerified(),
//                user.getBloodGroup(),
//                user.getDOB() != null ? user.getDOB().toString() : null,
//                user.getFirstName(),
//                user.getMiddleName(),
//                user.getLastName()
//        ));
//    }
//
//    public User getUserById(Long id) {
//        return userRepository.findById(id).orElse(null);
//    }
//}
//

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Method to get all users with pagination and filtering based on email verification status
    public Page<UserDto> getAllUsers(String filterType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> usersPage;

        switch (filterType.toLowerCase()) {
            case "verified":
                usersPage = userRepository.findByEmailVerified(true, pageable);
                break;
            case "unverified":
                usersPage = userRepository.findByEmailVerified(false, pageable);
                break;
            case "all":
            default:
                usersPage = userRepository.findAll(pageable);
                break;
        }

        return usersPage.map(this::convertToUserDto);
    }

    // Method to get user by ID
    public UserDto getUserById(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        return userOpt.map(this::convertToUserDto).orElse(null);
    }
    // Method to convert User entity to UserDto
    public UserDto convertToUserDto(User user) {
        List<String> roleNames = user.getRoles().stream()
                .map(Role::getRoleName)
                .collect(Collectors.toList());

        List<String> techNames = user.getTechnologies().stream()
                .map(ut -> ut.getTechnology().getTechName())
                .collect(Collectors.toList());

        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getMiddleName(),
                user.getLastName(),
                user.getEmail(),
                user.getUsername(),
                user.getMobileNo(),
                user.getDOB(),
                user.getEmailVerified(),
                user.getBloodGroup(),
                roleNames,
                techNames,
                user.getStatus() // ✅ Add this
        );
    }
}

