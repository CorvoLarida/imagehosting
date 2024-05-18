package kz.am.imagehosting.security;

import kz.am.imagehosting.domain.AuthRole;
import kz.am.imagehosting.domain.AuthUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class CustomUserDetails implements UserDetails {

    private AuthUser authUser;

    public CustomUserDetails(){

    }
    public CustomUserDetails(AuthUser authUser) {
        this.authUser = authUser;
    }

    @Override
    public String getUsername() {
        return authUser.getUsername();
    }

    @Override
    public String getPassword() {
        return authUser.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return authUser.isActive();
    }

    @Override
    public boolean isAccountNonLocked() {
        return authUser.isActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return authUser.isActive();
    }

    @Override
    public boolean isEnabled() {
        return authUser.isActive();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authUser.getUserRoles().stream()
                .map(role -> {
                    AuthRole r = new AuthRole();
                    r.setName("ROLE_" + role.getName());
                    return r;
                })
                .collect(Collectors.toSet());
    }
}
