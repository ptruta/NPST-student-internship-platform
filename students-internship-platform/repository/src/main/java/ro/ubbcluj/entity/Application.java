package ro.ubbcluj.entity;

import lombok.*;

import javax.persistence.*;

/**
 * The Application class creates an entity with various
 * attributes, and related behaviour.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_authentication_id")
    private UserAuthentication userAuthentication;

    @ManyToOne
    @JoinColumn(name = "internship_announcement_id")
    private InternshipAnnouncement internshipAnnouncement;

    /**
     * Constructor.
     *
     * @param userAuthentication     (required) the userAuthentication that application belongs to.
     */
    public Application(UserAuthentication userAuthentication) {
        this.userAuthentication = userAuthentication;
    }
}
