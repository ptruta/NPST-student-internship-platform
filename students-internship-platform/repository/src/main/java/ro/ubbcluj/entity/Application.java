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
    @JoinColumn(name = "person_id")
    private Person person;

    @ManyToOne
    @JoinColumn(name = "announcement_id")
    private Announcement announcement;

    /**
     * Constructor.
     *
     * @param person     (required) the person that application belongs to.
     */
    public Application(Person person) {
        this.person = person;
    }
}
