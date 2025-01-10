package sysc4806.project.models;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPresentationAvailability {
    @Test
    public void testGetAndSetDateTime() {
        LocalDateTime dateTime1 = LocalDateTime.of(2023, 12, 3, 4, 5);
        PresentationAvailability availability = new PresentationAvailability(dateTime1);
        assertEquals(dateTime1, availability.getDateTime());
        LocalDateTime dateTime2 = LocalDateTime.of(2023, 12, 12, 0, 0);
        availability.setDateTime(dateTime2);
        assertEquals(dateTime2, availability.getDateTime());
    }
}
