package kz.am.imagehosting.dto;

import java.util.List;
import java.util.UUID;

import kz.am.imagehosting.domain.AuthUser;

public class UserDTO {
    private UUID id;
    private String username;
    private List<RoleDTO> roles;

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public List<RoleDTO> getRoles() {
        return roles;
    }
    public void setRoles(List<RoleDTO> roles) {
        this.roles = roles;
    }

    public UserDTO mapForJson(AuthUser user) {
        UserDTO udto = new UserDTO();
        udto.setId(user.getId());
        udto.setUsername(user.getUsername());
        udto.setRoles(user.getUserRoles().stream()
            .map(role -> new RoleDTO(role.getId(), role.getName()))
            .toList()
        );
        return udto;
    }
}
