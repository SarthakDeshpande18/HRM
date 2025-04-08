package SRMS.FinalProject.Service;


import SRMS.FinalProject.DTO.UserDto;
import SRMS.FinalProject.Repository.UserRepository;
import SRMS.FinalProject.UserEntity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserDto> getAllUsers(String filterType) {
        List<User> users;

        switch (filterType) {
            case "verified":
                users = userRepository.findByEmailVerified(true);
                break;
            case "unverified":
                users = userRepository.findByEmailVerified(false);
                break;
            case "all":
            default:
                users = userRepository.findAll();
                break;
        }

        return users.stream().map(user -> new UserDto(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getMobileNo(),
                user.getEmailVerified(),
                user.getBloodGroup(),
                user.getDOB(),
                user.getFirstName(),
                user.getMiddleName(),
                user.getLastName()
        )).collect(Collectors.toList());
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
