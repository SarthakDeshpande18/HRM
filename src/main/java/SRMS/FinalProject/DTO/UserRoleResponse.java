package SRMS.FinalProject.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRoleResponse {
    private Long id;
    private Long userId;
    private String userName;
    private Long roleId;
    private String roleName;

    public UserRoleResponse(Long id, Long userId, String userName, Long roleId, String roleName) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.roleId = roleId;
        this.roleName = roleName;
    }
}