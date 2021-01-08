package ro.ubbcluj.junit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ro.ubbcluj.dto.UserDTO;
import ro.ubbcluj.entity.Account;
import ro.ubbcluj.entity.Announcement;
import ro.ubbcluj.entity.Person;
import ro.ubbcluj.entity.Role;
import ro.ubbcluj.enums.GenderEnum;
import ro.ubbcluj.enums.RoleEnum;
import ro.ubbcluj.interfaces.UserService;
import ro.ubbcluj.repository.AccountRepository;
import ro.ubbcluj.repository.AnnouncementRepository;
import ro.ubbcluj.repository.PersonRepository;
import ro.ubbcluj.repository.RoleRepository;
import ro.ubbcluj.service.UserServiceImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * This class contains junit tests for User Service
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    private static final String USERNAME = "user";
    private static final String USERNAME1 = "user1";
    private static final String FIRST_NAME = "Raluca";
    private static final String FIRST_NAME1 = "Diana";
    private static final String LAST_NAME = "Sechel";
    private static final String LAST_NAME1 = "Truta";
    private static final String EMAIL = "raluca.sechel@pitechnologies.ro";
    private static final String EMAIL1 = "diana.truta@pitechnologies.ro";
    private static final String ADDRESS = "Cluj";
    private static final String ADDRESS1 = "Zalau";
    private static final String ANNOUNCEMENT_TITLE = "Plaza";
    private static final String ANNOUNCEMENT_TECHNOLOGIES = "Cluj";
    private static final long ID = 1L;
    private static final long ID1 = 2L;
    @InjectMocks
    private static final UserService userService = new UserServiceImpl();
    @Mock
    private static AccountRepository accountRepository;
    @Mock
    private static PersonRepository personRepository;
    @Mock
    private static AnnouncementRepository announcementRepository;
    @Mock
    private static RoleRepository roleRepository;

    /**
     * Method used to mock account entities.
     *
     * @return list of accounts
     */
    private static List<Account> createAccounts() {

        Announcement announcement = Announcement.builder()
                .title(ANNOUNCEMENT_TITLE)
                .technologies(ANNOUNCEMENT_TECHNOLOGIES)
                .build();

        Person person = Person.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .birthDate(new Date())
                .email(EMAIL)
                .gender(GenderEnum.FEMALE)
                .role(new Role(RoleEnum.COMPANY))
                .announcements(Collections.singletonList(announcement))
                .build();

        Account account = Account.builder()
                .id(ID)
                .username(USERNAME)
                .person(person)
                .active(true)
                .registrationDate(new Date())
                .build();

        Account account1 = Account.builder()
                .id(ID1)
                .username(USERNAME)
                .person(person)
                .active(true)
                .registrationDate(new Date())
                .build();

        return Arrays.asList(account, account1);
    }

    @Test
    public void testGetUsers() {

        // given
        List<Account> accounts = createAccounts();

        // when
        when(accountRepository.findAll())
                .thenReturn(accounts);

        // call
        List<UserDTO> userDTOList = userService.getUsers();

        // then
        assertNotNull(userDTOList);
        assertEquals(accounts.size(), userDTOList.size());
        assertEquals(accounts.get(0).getPerson().getFirstName(), userDTOList.get(0).getFirstName());
    }

    @Test
    public void testGetAvailableUsers() {
        // given
        List<Account> accounts = createAccounts();

        // when
        when(accountRepository.findAllByActive(true)).thenReturn(accounts);

        // call
        List<UserDTO> userDTOList = userService.getAvailableUsers();

        // then
        assertNotNull(userDTOList);
        assertEquals(accounts.size(), userDTOList.size());
        assertEquals(accounts.get(0).isActive(), true);
    }

    @Test
    public void testCreateUser() {

        UserDTO userDTO = UserDTO.builder()
                .id(ID)
                .username(USERNAME)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .announcement(ID)
                .role(RoleEnum.COMPANY)
                .birthDate(new Date())
                .gender(GenderEnum.FEMALE)
                .registrationDate(new Date())
                .active(true)
                .build();

        //when
        when(accountRepository.findOne(ID)).thenReturn(createAccounts().get(0));
        userService.createUser(userDTO);

        //call
        UserDTO expectedUserDTO = userService.getById(ID);

        //then
        assertEquals(expectedUserDTO.getId(), userDTO.getId());
    }

    @Test
    public void testDeleteUser() {

        UserDTO userDTO = UserDTO.builder()
                .id(ID)
                .username(USERNAME)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .announcement(ID)
                .role(RoleEnum.COMPANY)
                .birthDate(new Date())
                .gender(GenderEnum.FEMALE)
                .registrationDate(new Date())
                .active(true)
                .build();

        //when
        when(accountRepository.findOne(ID)).thenReturn(createAccounts().get(0));
        userService.createUser(userDTO);

        userService.deleteUser(userDTO.getId());

        //call
        UserDTO expectedUserDTO = userService.getById(ID);

        //then
        assertEquals(expectedUserDTO.isActive(), false);

    }

    @Test
    public void testGetUsersByAnnouncement() {
        //given
        List<Account> accounts = createAccounts();
        UserDTO userDTO = UserDTO.builder()
                .id(ID)
                .username(USERNAME)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .announcement(ID)
                .role(RoleEnum.COMPANY)
                .birthDate(new Date())
                .gender(GenderEnum.FEMALE)
                .registrationDate(new Date())
                .active(true)
                .build();

        //when
        when(accountRepository.findAll())
                .thenReturn(accounts);

        // call
        List<UserDTO> userDTOList = userService.getUsersByAnnouncement(ID);

        // then
        assertNotNull(userDTOList);
        assertNotEquals(accounts.size(), userDTOList.size());
    }

    @Test
    public void testEditUser() {

        UserDTO userDTO = UserDTO.builder()
                .id(ID)
                .username(USERNAME)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .announcement(ID)
                .role(RoleEnum.COMPANY)
                .birthDate(new Date())
                .gender(GenderEnum.FEMALE)
                .registrationDate(new Date())
                .active(true)
                .build();

        UserDTO userDTO1 = UserDTO.builder()
                .id(ID)
                .username(USERNAME1)
                .role(RoleEnum.ADMIN)
                .build();

        //when
        when(accountRepository.findOne(ID)).thenReturn(createAccounts().get(0));
        userService.createUser(userDTO);
        userService.editUser(userDTO1);

        //call
        UserDTO expectedUserDTO = userService.getById(ID);

        //then
        assertEquals(expectedUserDTO.getUsername(), USERNAME1);
        assertEquals(expectedUserDTO.getRole(), RoleEnum.ADMIN);
    }

    @Test
    public void testUpdateAccount() {

        UserDTO userDTO = UserDTO.builder()
                .id(ID)
                .username(USERNAME)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .address(ADDRESS)
                .announcement(ID)
                .role(RoleEnum.STUDENT)
                .birthDate(new Date())
                .gender(GenderEnum.FEMALE)
                .registrationDate(new Date())
                .active(true)
                .build();

        UserDTO userDTO1 = UserDTO.builder()
                .id(ID)
                .username(USERNAME1)
                .firstName(FIRST_NAME1)
                .lastName(LAST_NAME1)
                .email(EMAIL1)
                .address(ADDRESS1)
                .build();

        //when
        when(accountRepository.findOne(ID)).thenReturn(createAccounts().get(0));
        userService.createUser(userDTO);
        userService.updateAccount(userDTO1);

        //call
        UserDTO expectedUserDTO = userService.getById(ID);

        //then
        assertEquals(expectedUserDTO.getUsername(), USERNAME1);
        assertEquals(expectedUserDTO.getFirstName(), FIRST_NAME1);
        assertEquals(expectedUserDTO.getLastName(), LAST_NAME1);
        assertEquals(expectedUserDTO.getEmail(), EMAIL1);
        assertEquals(expectedUserDTO.getAddress(), ADDRESS1);

    }
}
