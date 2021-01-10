package ro.ubbcluj.model.frontObjects;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InternshipAnnouncementSearchDTO {

    private String location;
    private Date startDate;
    private Date endDate;
    private Date postingDate;
    private String duration;
    private String domains;
    private boolean paidOrNot;
    private String workingTime;
}
