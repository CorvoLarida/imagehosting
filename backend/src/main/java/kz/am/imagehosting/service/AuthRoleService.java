package kz.am.imagehosting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kz.am.imagehosting.domain.AuthRole;
import kz.am.imagehosting.repository.RoleRepository;

@Service
public class AuthRoleService {

    private final RoleRepository roleRepository;

    private final String ROLE_PREFIX = "ROLE_";

    @Autowired
    public AuthRoleService(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    public String removeRolePrefix(String roleName) {
        String result = roleName;
        if (roleName != null && roleName.startsWith(ROLE_PREFIX)) result = result.substring(ROLE_PREFIX.length());
        return result;
    }

    public String addRolePrefix(String roleName) {
        String result = roleName;
        if (roleName != null && !roleName.startsWith(ROLE_PREFIX)) result = ROLE_PREFIX + result;
        return roleName;
    }

    public AuthRole getRole(String roleName) {
        return this.roleRepository.findRoleByName(roleName).orElse(null);
    }

}
