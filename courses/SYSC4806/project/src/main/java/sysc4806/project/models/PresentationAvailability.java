package sysc4806.project.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


@Entity
public class PresentationAvailability {
    @Id
    @GeneratedValue
    private long id;
    private LocalDateTime dateTime;
    private final static List<LocalDateTime> oralPresentationDates = Arrays.asList(
            LocalDateTime.of(2024, 1, 22, 10, 0),
            LocalDateTime.of(2024, 1, 22, 12, 30),
            LocalDateTime.of(2024, 1, 23, 14, 0),
            LocalDateTime.of(2024, 1, 24, 14, 0),
            LocalDateTime.of(2024, 1, 25, 10, 0),
            LocalDateTime.of(2024, 1, 25, 15, 0),
            LocalDateTime.of(2024, 1, 26, 12, 0)
    );


    public PresentationAvailability() {
    }

    public PresentationAvailability(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    /**
     *
     * @return the date/time
     */
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /**
     *
     * @param dateTime the date/time to set
     */
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Sets the id
     * @param id Long id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the id
     * @return Long the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Get oral presentation dates
     * @return a List of oral presentation dates
     */
    public static List<LocalDateTime> getOralPresentationDates() {
        return oralPresentationDates;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof PresentationAvailability && ((PresentationAvailability) other).dateTime.equals(this.dateTime);
    }
}
