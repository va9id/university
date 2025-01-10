package sysc4806.project.repositories;

import org.springframework.data.repository.CrudRepository;
import sysc4806.project.models.ApplicationUser;

public interface ApplicationUserRepository extends CrudRepository<ApplicationUser, Long> {
    public ApplicationUser findApplicationUserByEmail(String email);
}
