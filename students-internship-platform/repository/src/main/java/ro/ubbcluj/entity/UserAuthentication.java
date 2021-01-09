package ro.ubbcluj.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * The Applicant class creates an entity with various
 * attributes, and related behaviour.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthentication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "userAuthentication")
    private Account account;

    private String firstName;
    private String lastName;
    private String email;
    private String status;
    private String education;
    private String workHistory;
    private String volunteerExperience;
    private String domainsOfInterest;
    private boolean availability;
    private String hobbies;
    private String contact;

    @OneToMany(mappedBy = "userAuthentication")
    private List<Application> applications;

    @OneToMany(mappedBy = "userAuthentication")
    private List<InternshipAnnouncement> internshipAnnouncements;

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
     */
    public UserAuthentication(String firstName, String lastName, String email,
                              String status, String education, String workHistory,
                              String volunteerExperience, String domainsOInterest,
                              boolean availability, String hobbies, String contact,
                              List<InternshipAnnouncement> internshipAnnouncements, Role role, String skills) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.status = status;
        this.education = education;
        this.workHistory = workHistory;
        this.volunteerExperience = volunteerExperience;
        this.domainsOfInterest = domainsOInterest;
        this.availability = availability;
        this.hobbies = hobbies;
        this.contact = contact;
        this.internshipAnnouncements = internshipAnnouncements;
        this.role = role;
        this.skills = skills;
    }
}
