package ro.ubbcluj.dto;

import lombok.*;
import ro.ubbcluj.enums.RoleEnum;

import java.util.Date;

/**
 * This class is used to transfer data from the user interface to server and backwards
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthenticationDTO {

    private Long id;
    private String username;
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;
    private String email;
    private RoleEnum role;
    private Long announcement;
    private String skills;
    private Date registrationDate;
    private boolean availability;
    private String status;
    private String education;
    private String workHistory;
    private String volunteerExperience;
    private String domainsOInterest;
    private String hobbies;
    private String contact;

    @Override
    public String toString() {
        return String.format(
                "User [Username='%s', First Name='%s', Last Name='%s', Email='%s', Role='%s']",
                username, firstName, lastName, email, role
        );
    }

}
