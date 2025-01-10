package sysc4806.project.models;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUserDetails {
    @Test
    public void testGetUserId() {
        long userId = 1234;
        UserDetails userDetails = new UserDetails("username", "password", new ArrayList<SimpleGrantedAuthority>(), userId);
        assertEquals(userId, userDetails.getUserId());
    }
}
