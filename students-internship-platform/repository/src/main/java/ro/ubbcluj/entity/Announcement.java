package ro.ubbcluj.entity;

import lombok.*;

import javax.persistence.*;
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
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String technologies;
    private Date startDate;
    private Date endDate;
    private Date deadline;
    private boolean active;

    @ManyToOne
    private Person person;

    @OneToMany(mappedBy = "announcement")
    private List<Application> applications = new ArrayList<>();

}
