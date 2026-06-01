package kz.am.imagehosting.security;

import kz.am.imagehosting.domain.AuthRole;
import kz.am.imagehosting.domain.AuthUser;
import kz.am.imagehosting.repository.UserRepository;
import kz.am.imagehosting.service.AuthUserService;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserDetailsImpl implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public UserDetailsImpl(UserRepository userRepository) {this.userRepository=userRepository;}

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser authUser = userRepository.findFirstUserByUsername(username).orElse(null);

        if (authUser == null) {
            throw new UsernameNotFoundException("User not found");
        }

        Set<? extends GrantedAuthority> authorities = authUser.getUserRoles().stream()
                .map(role -> {
                    AuthRole r = new AuthRole();
                    r.setName("ROLE_" + role.getName());
                    return r;
                })
                .collect(Collectors.toSet());
        return new User(authUser.getUsername(), authUser.getPassword(), authorities);
    }
}
