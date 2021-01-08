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
public class AnnouncementDTO {

    private Long id;
    private String title;
    private String description;
    private String technologies;
    private Date startDate;
    private Date endDate;
    private Date deadline;
    private String username;
    private boolean active;

}

