package kz.am.imagehosting.security;

import kz.am.imagehosting.domain.AuthUser;
import kz.am.imagehosting.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsImpl implements UserDetailsService {
    private UserRepository userRepository;

    public UserDetailsImpl(UserRepository userRepository) {this.userRepository=userRepository;}

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser authUser = userRepository.findFirstUserByUsername(username).orElse(null);

        if (authUser == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(authUser);
    }
}
