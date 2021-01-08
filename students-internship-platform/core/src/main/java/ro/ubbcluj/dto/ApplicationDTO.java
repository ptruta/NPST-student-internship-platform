package ro.ubbcluj.dto;

import lombok.*;

import java.util.Date;

/**
 * This class is used to transfer data from the user interface to server and backwards
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationDTO {

    private Long id;
    private Long announcementId;
    private String personEmail;
    private String title;
    private Date startDate;
    private Date endDate;
    private Long userId;
    private String username;
    private String personName;
    private String companyName;

}

