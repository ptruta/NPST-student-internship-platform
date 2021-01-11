package ro.ubbcluj.junit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ro.ubbcluj.dto.UserAuthenticationDTO;
import ro.ubbcluj.entity.Account;
import ro.ubbcluj.entity.InternshipAnnouncement;
import ro.ubbcluj.entity.Role;
import ro.ubbcluj.entity.UserAuthentication;
import ro.ubbcluj.enums.RoleEnum;
import ro.ubbcluj.interfaces.UserAuthenticationService;
import ro.ubbcluj.repository.AccountRepository;
import ro.ubbcluj.repository.InternshipAnnouncementRepository;
import ro.ubbcluj.repository.RoleRepository;
import ro.ubbcluj.repository.UserAuthenticationRepository;
import ro.ubbcluj.service.UserAuthenticationServiceImpl;

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
public class UserAuthenticationServiceTest {

    private static final String USERNAME = "user";
    private static final String USERNAME1 = "user1";
    private static final String FIRST_NAME = "Raluca";
    private static final String FIRST_NAME1 = "Patricia";
    private static final String LAST_NAME = "Sechel";
    private static final String LAST_NAME1 = "Truta";
    private static final String EMAIL = "raluca.sechel@pitechnologies.ro";
    private static final String EMAIL1 = "patricia.truta@pitechnologies.ro";
    private static final String ADDRESS = "Cluj";
    private static final String ADDRESS1 = "Zalau";
    private static final String ANNOUNCEMENT_TITLE = "Plaza";
    private static final String ANNOUNCEMENT_TECHNOLOGIES = "Cluj";
    private static final long ID = 1L;
    private static final long ID1 = 2L;
    @InjectMocks
    private static final UserAuthenticationService USER_AUTHENTICATION_SERVICE = new UserAuthenticationServiceImpl();
    @Mock
    private static AccountRepository accountRepository;
    @Mock
    private static InternshipAnnouncementRepository internshipAnnouncementRepository;
    @Mock
    private static RoleRepository roleRepository;

    /**
     * Method used to mock account entities.
     *
     * @return list of accounts
     */
    private static List<Account> createAccounts() {

        InternshipAnnouncement internshipAnnouncement = InternshipAnnouncement.builder()
                .title(ANNOUNCEMENT_TITLE)
                .build();

        UserAuthentication person = UserAuthentication.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .role(new Role(RoleEnum.RECRUITER))
                .internshipAnnouncements(Collections.singletonList(internshipAnnouncement))
                .build();

        Account account = Account.builder()
                .id(ID)
                .username(USERNAME)
                .userAuthentication(person)
                .active(true)
                .registrationDate(new Date())
                .build();

        Account account1 = Account.builder()
                .id(ID1)
                .username(USERNAME)
                .userAuthentication(person)
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
        List<UserAuthenticationDTO> userAuthenticationDTOList = USER_AUTHENTICATION_SERVICE.getUsers();

        // then
        assertNotNull(userAuthenticationDTOList);
        assertEquals(accounts.size(), userAuthenticationDTOList.size());
        assertEquals(accounts.get(0).getUserAuthentication().getFirstName(), userAuthenticationDTOList.get(0).getFirstName());
    }

    @Test
    public void testGetAvailableUsers() {
        // given
        List<Account> accounts = createAccounts();

        // when
        when(accountRepository.findAllByActive(true)).thenReturn(accounts);

        // call
        List<UserAuthenticationDTO> userAuthenticationDTOList = USER_AUTHENTICATION_SERVICE.getAvailableUsers();

        // then
        assertNotNull(userAuthenticationDTOList);
        assertEquals(accounts.size(), userAuthenticationDTOList.size());
        assertEquals(accounts.get(0).isActive(), true);
    }

    @Test
    public void testCreateUser() {

        UserAuthenticationDTO userAuthenticationDTO = UserAuthenticationDTO.builder()
                .id(ID)
                .username(USERNAME)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .announcement(ID)
                .role(RoleEnum.RECRUITER)
                .registrationDate(new Date())
                .availability(true)
                .build();

        //when
        when(accountRepository.findOne(ID)).thenReturn(createAccounts().get(0));
        USER_AUTHENTICATION_SERVICE.createUser(userAuthenticationDTO);

        //call
        UserAuthenticationDTO expectedUserAuthenticationDTO = USER_AUTHENTICATION_SERVICE.getById(ID);

        //then
        assertEquals(expectedUserAuthenticationDTO.getId(), userAuthenticationDTO.getId());
    }

    @Test
    public void testDeleteUser() {

        UserAuthenticationDTO userAuthenticationDTO = UserAuthenticationDTO.builder()
                .id(ID)
                .username(USERNAME)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .announcement(ID)
                .role(RoleEnum.RECRUITER)
                .registrationDate(new Date())
                .availability(true)
                .build();

        //when
        when(accountRepository.findOne(ID)).thenReturn(createAccounts().get(0));
        USER_AUTHENTICATION_SERVICE.createUser(userAuthenticationDTO);

        USER_AUTHENTICATION_SERVICE.deleteUser(userAuthenticationDTO.getId());

        //call
        UserAuthenticationDTO expectedUserAuthenticationDTO = USER_AUTHENTICATION_SERVICE.getById(ID);

        //then
        assertEquals(expectedUserAuthenticationDTO.isAvailability(), false);

    }

    @Test
    public void testGetUsersByAnnouncement() {
        //given
        List<Account> accounts = createAccounts();
        UserAuthenticationDTO userAuthenticationDTO = UserAuthenticationDTO.builder()
                .id(ID)
                .username(USERNAME)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .announcement(ID)
                .role(RoleEnum.RECRUITER)
                .registrationDate(new Date())
                .availability(true)
                .build();

        //when
        when(accountRepository.findAll())
                .thenReturn(accounts);

        // call
        List<UserAuthenticationDTO> userAuthenticationDTOList = USER_AUTHENTICATION_SERVICE.getUsersByAnnouncement(ID);

        // then
        assertNotNull(userAuthenticationDTOList);
        assertNotEquals(accounts.size(), userAuthenticationDTOList.size());
    }

    @Test
    public void testEditUser() {

        UserAuthenticationDTO userAuthenticationDTO = UserAuthenticationDTO.builder()
                .id(ID)
                .username(USERNAME)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .announcement(ID)
                .role(RoleEnum.RECRUITER)
                .registrationDate(new Date())
                .availability(true)
                .build();

        UserAuthenticationDTO userAuthenticationDTO1 = UserAuthenticationDTO.builder()
                .id(ID)
                .username(USERNAME1)
                .role(RoleEnum.RECRUITER)
                .build();

        //when
        when(accountRepository.findOne(ID)).thenReturn(createAccounts().get(0));
        USER_AUTHENTICATION_SERVICE.createUser(userAuthenticationDTO);
        USER_AUTHENTICATION_SERVICE.editUser(userAuthenticationDTO1);

        //call
        UserAuthenticationDTO expectedUserAuthenticationDTO = USER_AUTHENTICATION_SERVICE.getById(ID);

        //then
        assertEquals(expectedUserAuthenticationDTO.getUsername(), USERNAME1);
    }

    @Test
    public void testUpdateAccount() {

        UserAuthenticationDTO userAuthenticationDTO = UserAuthenticationDTO.builder()
                .id(ID)
                .username(USERNAME)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .announcement(ID)
                .role(RoleEnum.APPLICANT)
                .registrationDate(new Date())
                .availability(true)
                .build();

        UserAuthenticationDTO userAuthenticationDTO1 = UserAuthenticationDTO.builder()
                .id(ID)
                .username(USERNAME1)
                .firstName(FIRST_NAME1)
                .lastName(LAST_NAME1)
                .email(EMAIL1)
                .build();

        //when
        when(accountRepository.findOne(ID)).thenReturn(createAccounts().get(0));
        USER_AUTHENTICATION_SERVICE.createUser(userAuthenticationDTO);
        USER_AUTHENTICATION_SERVICE.updateAccount(userAuthenticationDTO1);

        //call
        UserAuthenticationDTO expectedUserAuthenticationDTO = USER_AUTHENTICATION_SERVICE.getById(ID);

        //then
        assertEquals(expectedUserAuthenticationDTO.getUsername(), USERNAME1);
        assertEquals(expectedUserAuthenticationDTO.getFirstName(), FIRST_NAME1);
        assertEquals(expectedUserAuthenticationDTO.getLastName(), LAST_NAME1);
        assertEquals(expectedUserAuthenticationDTO.getEmail(), EMAIL1);

    }
}
