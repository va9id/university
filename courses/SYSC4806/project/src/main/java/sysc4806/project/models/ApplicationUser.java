package sysc4806.project.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class ApplicationUser {
    @Id
    @GeneratedValue
    private long id;

    private String name;

    private String email;

    private String password;

    public ApplicationUser() {}

    public ApplicationUser(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    /**
     * Get student's id
     * @return long
     */
    public long getId() {
        return id;
    }

    /**
     * Set student's id
     * @param id long
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Get student's name
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Set student's name
     * @param name String
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get student's email
     * @return String
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set student's email
     * @param email String
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get student's password
     * @return String
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set student's password
     * @param password String
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
