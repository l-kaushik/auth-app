package in.lokeshkaushik.authapp;

import in.lokeshkaushik.authapp.configs.AppConstants;
import in.lokeshkaushik.authapp.entities.Role;
import in.lokeshkaushik.authapp.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthAppBackendApplication implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

	public static void main(String[] args) {
        SpringApplication.run(AuthAppBackendApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {

        // we will create some default user role
        // ADMIN
        // GUEST

        roleRepository.findByName("ROLE_" + AppConstants.ADMIN_ROLE).ifPresentOrElse(role -> {
        }, () -> {
            Role role = new Role();
            role.setName("ROLE_" + AppConstants.ADMIN_ROLE);
            roleRepository.save(role);
        });

        roleRepository.findByName("ROLE_" + AppConstants.GUEST_ROLE).ifPresentOrElse(role -> {
        }, () -> {
            Role role = new Role();
            role.setName("ROLE_" + AppConstants.GUEST_ROLE);
            roleRepository.save(role);
        });
    }
}
