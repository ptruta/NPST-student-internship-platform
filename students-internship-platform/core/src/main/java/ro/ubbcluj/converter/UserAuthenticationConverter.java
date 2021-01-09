package ro.ubbcluj.converter;

import org.springframework.data.domain.Page;
import ro.ubbcluj.dto.UserAuthenticationDTO;
import ro.ubbcluj.entity.Account;
import ro.ubbcluj.entity.UserAuthentication;

import java.util.Date;

import static ro.ubbcluj.util.ValidationUtil.notNull;

/**
 * The converter class is used to convert Account and Person entities into User DTO,
 * a User DTO into Account and Person entities and a page of Accounts into a page of User DTOs
 */
public class UserAuthenticationConverter {

    /**
     * This method converts an Account and a Person to a
     * single User DTO
     *
     * @param account
     * @return
     */
    public static UserAuthenticationDTO convertToDTO(Account account) {
        notNull(account);
        UserAuthentication userAuthentication = account.getUserAuthentication();
        notNull(userAuthentication);

        return UserAuthenticationDTO.builder()
                .id(account.getId())
                .username(account.getUsername())
                .password(account.getPassword())
                .firstName(userAuthentication.getFirstName())
                .lastName(userAuthentication.getLastName())
                .email(userAuthentication.getEmail())
                .role(userAuthentication.getRole().getRole())
                .registrationDate(account.getRegistrationDate())
                .availability(account.isActive())
                .build();
    }

    /**
     * This method converts a User DTO to an Account
     *
     * @param userAuthenticationDTO
     * @return
     */
    public static Account convertToEntityAccount(UserAuthenticationDTO userAuthenticationDTO) {
        notNull(userAuthenticationDTO);

        return Account.builder()
                .id(userAuthenticationDTO.getId())
                .username(userAuthenticationDTO.getUsername())
                .password(userAuthenticationDTO.getPassword())
                .registrationDate(new Date())
                .build();
    }

    /**
     * This method converts a User DTO to a Person
     *
     * @param userAuthenticationDTO
     * @return
     */
    public static UserAuthentication convertToEntityUserAuthentication(UserAuthenticationDTO userAuthenticationDTO) {
        notNull(userAuthenticationDTO);

        return UserAuthentication.builder()
                .id(userAuthenticationDTO.getId())
                .firstName(userAuthenticationDTO.getFirstName())
                .lastName(userAuthenticationDTO.getLastName())
                .email(userAuthenticationDTO.getEmail())
                .skills(userAuthenticationDTO.getSkills())
                .availability(userAuthenticationDTO.isAvailability())
                .build();
    }

    /**
     * This method converts a page of Accounts to a page of User DTOs
     *
     * @param accountsPage
     * @return
     */
    public static Page<UserAuthenticationDTO> convertToDTOPage(Page<Account> accountsPage) {
        return accountsPage.map(UserAuthenticationConverter::convertToDTO);
    }

}
