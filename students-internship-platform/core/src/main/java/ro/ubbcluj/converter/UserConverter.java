package ro.ubbcluj.converter;

import org.springframework.data.domain.Page;
import ro.ubbcluj.dto.UserDTO;
import ro.ubbcluj.entity.Account;
import ro.ubbcluj.entity.Person;

import java.util.Date;

import static ro.ubbcluj.util.ValidationUtil.notNull;

/**
 * The converter class is used to convert Account and Person entities into User DTO,
 * a User DTO into Account and Person entities and a page of Accounts into a page of User DTOs
 */
public class UserConverter {

    /**
     * This method converts an Account and a Person to a
     * single User DTO
     *
     * @param account
     * @return
     */
    public static UserDTO convertToDTO(Account account) {
        notNull(account);
        Person person = account.getPerson();
        notNull(person);

        return UserDTO.builder()
                .id(account.getId())
                .username(account.getUsername())
                .password(account.getPassword())
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .email(person.getEmail())
                .address(person.getAddress())
                .birthDate(person.getBirthDate())
                .gender(person.getGender())
                .role(person.getRole().getRole())
                .registrationDate(account.getRegistrationDate())
                .active(account.isActive())
                .build();
    }

    /**
     * This method converts a User DTO to an Account
     *
     * @param userDTO
     * @return
     */
    public static Account convertToEntityAccount(UserDTO userDTO) {
        notNull(userDTO);

        return Account.builder()
                .id(userDTO.getId())
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .registrationDate(new Date())
                .build();
    }

    /**
     * This method converts a User DTO to a Person
     *
     * @param userDTO
     * @return
     */
    public static Person convertToEntityPerson(UserDTO userDTO) {
        notNull(userDTO);

        return Person.builder()
                .id(userDTO.getId())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .address(userDTO.getAddress())
                .skills(userDTO.getSkills())
                .bio(userDTO.getBio())
                .birthDate(userDTO.getBirthDate())
                .gender(userDTO.getGender())
                .active(userDTO.isActive())
                .build();
    }

    /**
     * This method converts a page of Accounts to a page of User DTOs
     *
     * @param accountsPage
     * @return
     */
    public static Page<UserDTO> convertToDTOPage(Page<Account> accountsPage) {
        return accountsPage.map(UserConverter::convertToDTO);
    }

}
