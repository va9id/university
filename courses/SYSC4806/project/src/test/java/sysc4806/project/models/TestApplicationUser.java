package sysc4806.project.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestApplicationUser {

    private ApplicationUser applicationUser;

    @BeforeEach
    public void setUp() {
        applicationUser = new ApplicationUser();
    }

    @Test
    public void testGetAndSetId() {
        applicationUser.setId(-1);
        assertEquals(applicationUser.getId(), -1);
    }

    @Test
    public void testGetAndSetName() {
        applicationUser.setName("Joe");
        assertEquals(applicationUser.getName(), "Joe");
    }

    @Test
    public void testGetAndSetEmail() {
        applicationUser.setEmail("Joe@email.com");
        assertEquals(applicationUser.getEmail(), "Joe@email.com");
    }

    @Test
    public void testGetAndSetPassword() {
        applicationUser.setPassword("Password");
        assertEquals(applicationUser.getPassword(), "Password");
    }
}
