package ro.ubbcluj.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The Announcement class creates an entity with various
 * attributes, and related behaviour.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InternshipAnnouncement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String company;
    private String location;
    private String duration;
    private String domains;
    private String possibilityOfRemoteWork;
    private boolean paidOrNot;
    private String workingTime;
    private Integer numberOfPositions;
    private String requirements;
    private String availabilityOfTrainingCourse;
    private String possibilityOfContract;
    private String benefits;
    private String neededSkills;

    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private Date startDate;

    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private Date endDate;

    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private Date postingDate;

    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private Date deadline;

    private String username;
    private boolean availability;

    @ManyToOne
    private UserAuthentication userAuthentication;

    @OneToMany(mappedBy = "internshipAnnouncement")
    private List<Application> applications = new ArrayList<>();

}
