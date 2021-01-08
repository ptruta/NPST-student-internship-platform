package ro.ubbcluj.entity;

import lombok.*;
import ro.ubbcluj.enums.GenderEnum;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The Person class creates an entity with various
 * attributes, and related behaviour.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "person")
    private Account account;

    private String firstName;
    private String lastName;
    private String email;
    private Date birthDate;
    private GenderEnum gender;
    private String address;
    private String bio;
    private boolean active;

    @OneToMany(mappedBy = "person")
    private List<Application> applications;

    @OneToMany(mappedBy = "person")
    private List<Announcement> announcements;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id")
    private Role role;

    private String skills;

    /**
     * Constructor.
     *
     * @param firstName (required) the firstName of the person.
     * @param lastName  (required) the lastName of the person.
     * @param email     (required) the email of the person.
     * @param birthDate (required) the birth date of the person.
     * @param gender    (required) the gender of the person.
     * @param role      (required) the role of the person.
     * @param address   (required) the address of the person.
     */
    public Person(String firstName, String lastName, String email,
                  List<Announcement> announcements, Date birthDate, GenderEnum gender, Role role, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.announcements = announcements;
        this.birthDate = birthDate;
        this.gender = gender;
        this.role = role;
        this.address = address;
    }

}
