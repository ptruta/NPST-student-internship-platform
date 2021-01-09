package ro.ubbcluj.dto;

import lombok.*;

import java.util.Date;

/**
 * This class is used to transfer data from the user interface to server and backwards
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InternshipAnnouncementDTO {

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
    private Date startDate;
    private Date endDate;
    private Date postingDate;
    private Date deadline;
    private String username;
    private boolean availability;

}

