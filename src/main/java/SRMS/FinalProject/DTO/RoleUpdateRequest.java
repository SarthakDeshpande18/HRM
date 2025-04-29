package SRMS.FinalProject.DTO;


public class RoleUpdateRequest {
    private Long userId;
    private Long roleId; // Now we only need the new role ID

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
