package ro.ubbcluj.service.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ro.ubbcluj.dto.UserAuthenticationDTO;
import ro.ubbcluj.enums.RoleEnum;
import ro.ubbcluj.interfaces.UserAuthenticationService;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * This class tests all flows from User Service
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@SqlGroup({@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        scripts = "classpath:drop-create.sql")})
public class UserAuthenticationServiceIntegrationTest {

    private static final String USERNAME = "myUser";
    private static final String USERNAME1 = "myUser";
    private static final String FIRST_NAME = "Raluca";
    private static final String FIRST_NAME1 = "Diana";
    private static final String LAST_NAME = "Sechel";
    private static final String LAST_NAME1 = "Truta";
    private static final String EMAIL = "raluca.sechel@pitechnologies.ro";
    private static final String EMAIL1 = "diana.truta@pitechnologies.ro";
    private static final String ADDRESS = "Cluj";
    private static final String ADDRESS1 = "Zalau";
    private static final Long ID = 1L;
    @Autowired
    private UserAuthenticationService userAuthenticationService;

    /**
     * Method used to check if attributes are not null in a list
     *
     * @param userAuthenticationDTOList
     */
    private static void checkAttributes(List<UserAuthenticationDTO> userAuthenticationDTOList) {

        for (final UserAuthenticationDTO userAuthenticationDTO : userAuthenticationDTOList) {
            assertNotNull(userAuthenticationDTO.getId());
            assertNotNull(userAuthenticationDTO.getFirstName());
            assertNotNull(userAuthenticationDTO.getLastName());
            assertNotNull(userAuthenticationDTO.getEmail());
            assertNotNull(userAuthenticationDTO.getUsername());
        }
    }

    @Test
    public void testGetAllUsersNotNull() {

        final List<UserAuthenticationDTO> userAuthenticationDTOList = userAuthenticationService.getUsers();

        assertNotNull(userAuthenticationDTOList);

        checkAttributes(userAuthenticationDTOList);
    }

    @Test
    public void testCreateUser() {

        UserAuthenticationDTO userAuthenticationDTO = UserAuthenticationDTO.builder()
                .username(USERNAME)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .announcement(ID)
                .role(RoleEnum.STUDENT)
                .registrationDate(new Date())
                .build();

        userAuthenticationService.createUser(userAuthenticationDTO);

        assertTrue(userAuthenticationService.getUsers().size() >= 2);
        int size = userAuthenticationService.getUsers().size() - 1;

        UserAuthenticationDTO expectedUserAuthenticationDTO = userAuthenticationService.getUsers().get(size);
        assertEquals(expectedUserAuthenticationDTO.getUsername(), userAuthenticationDTO.getUsername());
        assertEquals(expectedUserAuthenticationDTO.getFirstName(), userAuthenticationDTO.getFirstName());
        assertEquals(expectedUserAuthenticationDTO.getLastName(), userAuthenticationDTO.getLastName());
        assertEquals(expectedUserAuthenticationDTO.getEmail(), userAuthenticationDTO.getEmail());

    }

    @Test
    public void testGetUserById() {

        UserAuthenticationDTO userAuthenticationDTO = userAuthenticationService.getById(ID);
        assertNotNull(userAuthenticationDTO);

        assertNotNull(userAuthenticationDTO.getId());
        assertNotNull(userAuthenticationDTO.getFirstName());
        assertNotNull(userAuthenticationDTO.getLastName());
        assertNotNull(userAuthenticationDTO.getEmail());
        assertNotNull(userAuthenticationDTO.getUsername());

        assertEquals(userAuthenticationDTO.getUsername(), "myUser");

    }

    @Test
    public void testDeleteUser() {

        userAuthenticationService.deleteUser(ID);

        UserAuthenticationDTO userAuthenticationDTO = userAuthenticationService.getById(ID);

        assertFalse(userAuthenticationDTO.isAvailability());

    }

    @Test
    public void testGetUsersByAnnouncement() {

        final List<UserAuthenticationDTO> userAuthenticationDTOList = userAuthenticationService.getUsersByAnnouncement(ID);

        assertNotNull(userAuthenticationDTOList);
        checkAttributes(userAuthenticationDTOList);
    }

    @Test
    public void testEditUser() {
        UserAuthenticationDTO userAuthenticationDTO1 = UserAuthenticationDTO.builder()
                .id(ID)
                .username(USERNAME1)
                .role(RoleEnum.COMPANY)
                .build();

        userAuthenticationService.editUser(userAuthenticationDTO1);

        assertTrue(userAuthenticationService.getUsers().size() >= 1);

        UserAuthenticationDTO expectedUserAuthenticationDTO = userAuthenticationService.getUsers().get(0);
        assertEquals(expectedUserAuthenticationDTO.getUsername(), userAuthenticationDTO1.getUsername());
        Assert.assertEquals(expectedUserAuthenticationDTO.getRole(), userAuthenticationDTO1.getRole());
    }

    @Test
    public void testUpdateAccount() {

        UserAuthenticationDTO userAuthenticationDTO1 = UserAuthenticationDTO.builder()
                .id(ID)
                .username(USERNAME1)
                .firstName(FIRST_NAME1)
                .lastName(LAST_NAME1)
                .email(EMAIL1)
                .build();

        userAuthenticationService.updateAccount(userAuthenticationDTO1);

        assertTrue(userAuthenticationService.getUsers().size() >= 1);

        UserAuthenticationDTO expectedUserAuthenticationDTO = userAuthenticationService.getUsers().get(0);
        assertEquals(expectedUserAuthenticationDTO.getUsername(), userAuthenticationDTO1.getUsername());
        assertEquals(expectedUserAuthenticationDTO.getFirstName(), userAuthenticationDTO1.getFirstName());
        assertEquals(expectedUserAuthenticationDTO.getLastName(), userAuthenticationDTO1.getLastName());
        assertEquals(expectedUserAuthenticationDTO.getEmail(), userAuthenticationDTO1.getEmail());
    }
}

