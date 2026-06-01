package kz.am.imagehosting;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import kz.am.imagehosting.domain.AuthRole;
import kz.am.imagehosting.domain.AuthUser;
import kz.am.imagehosting.service.AuthRoleService;
import kz.am.imagehosting.service.AuthUserService;

@SpringBootApplication
public class ImagehostingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImagehostingApplication.class, args);
	}

}

@Component
@Profile("development")
class TestInitDbData implements ApplicationRunner {

	@Autowired
    private AuthRoleService roleService;
	@Autowired
    private AuthUserService userService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void run(ApplicationArguments args) throws Exception {

		final class AuthUserCreator {

			private AuthUser createAuthUser(String username, String role) {
				AuthUser authUserAccount = new AuthUser();
				authUserAccount.setUsername(username);
				authUserAccount.setPassword(passwordEncoder.encode(username));
				authUserAccount.setActive(true);
				Set<AuthRole> roles = new HashSet<>();
				roles.add(roleService.getRole(role));
				authUserAccount.setUserRoles(roles);
				return authUserAccount;
			}

            private AuthUser createUSER(String username) {
				return this.createAuthUser(username, "USER");
            }

			private AuthUser createADMIN(String username) {
                return this.createAuthUser(username, "ADMIN");
            }
        }

		AuthUserCreator userCreator = new AuthUserCreator();
		AuthUser user1 = userCreator.createUSER("dbuser11");
		AuthUser user2 = userCreator.createUSER("dbuser22");
		AuthUser user3 = userCreator.createUSER("dbuser33");
		AuthUser admin1 = userCreator.createADMIN("dbadmin11");
		userService.saveUser(user1);
		userService.saveUser(user2);
		userService.saveUser(user3);
		userService.saveUser(admin1);
	}
}