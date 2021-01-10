package ro.ubbcluj.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date startDate;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date endDate;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date postingDate;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date deadline;
    private String username;
    private boolean availability;

}

