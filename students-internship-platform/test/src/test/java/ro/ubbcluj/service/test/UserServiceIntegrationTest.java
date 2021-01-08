package ro.ubbcluj.service.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ro.ubbcluj.dto.UserDTO;
import ro.ubbcluj.enums.GenderEnum;
import ro.ubbcluj.enums.RoleEnum;
import ro.ubbcluj.interfaces.UserService;

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
public class UserServiceIntegrationTest {

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
    private UserService userService;

    /**
     * Method used to check if attributes are not null in a list
     *
     * @param userDTOList
     */
    private static void checkAttributes(List<UserDTO> userDTOList) {

        for (final UserDTO userDTO : userDTOList) {
            assertNotNull(userDTO.getId());
            assertNotNull(userDTO.getFirstName());
            assertNotNull(userDTO.getLastName());
            assertNotNull(userDTO.getAddress());
            assertNotNull(userDTO.getEmail());
            assertNotNull(userDTO.getBirthDate());
            assertNotNull(userDTO.getGender());
            assertNotNull(userDTO.getUsername());
        }
    }

    @Test
    public void testGetAllUsersNotNull() {

        final List<UserDTO> userDTOList = userService.getUsers();

        assertNotNull(userDTOList);

        checkAttributes(userDTOList);
    }

    @Test
    public void testCreateUser() {

        UserDTO userDTO = UserDTO.builder()
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
                .build();

        userService.createUser(userDTO);

        assertTrue(userService.getUsers().size() >= 2);
        int size = userService.getUsers().size() - 1;

        UserDTO expectedUserDTO = userService.getUsers().get(size);
        assertEquals(expectedUserDTO.getUsername(), userDTO.getUsername());
        assertEquals(expectedUserDTO.getFirstName(), userDTO.getFirstName());
        assertEquals(expectedUserDTO.getLastName(), userDTO.getLastName());
        assertEquals(expectedUserDTO.getEmail(), userDTO.getEmail());

    }

    @Test
    public void testGetUserById() {

        UserDTO userDTO = userService.getById(ID);
        assertNotNull(userDTO);

        assertNotNull(userDTO.getId());
        assertNotNull(userDTO.getFirstName());
        assertNotNull(userDTO.getLastName());
        assertNotNull(userDTO.getAddress());
        assertNotNull(userDTO.getEmail());
        assertNotNull(userDTO.getBirthDate());
        assertNotNull(userDTO.getGender());
        assertNotNull(userDTO.getUsername());

        assertEquals(userDTO.getUsername(), "myUser");

    }

    @Test
    public void testDeleteUser() {

        userService.deleteUser(ID);

        UserDTO userDTO = userService.getById(ID);

        assertFalse(userDTO.isActive());

    }

    @Test
    public void testGetUsersByAnnouncement() {

        final List<UserDTO> userDTOList = userService.getUsersByAnnouncement(ID);

        assertNotNull(userDTOList);
        checkAttributes(userDTOList);
    }

    @Test
    public void testEditUser() {
        UserDTO userDTO1 = UserDTO.builder()
                .id(ID)
                .username(USERNAME1)
                .role(RoleEnum.COMPANY)
                .build();

        userService.editUser(userDTO1);

        assertTrue(userService.getUsers().size() >= 1);

        UserDTO expectedUserDTO = userService.getUsers().get(0);
        assertEquals(expectedUserDTO.getUsername(), userDTO1.getUsername());
        Assert.assertEquals(expectedUserDTO.getRole(), userDTO1.getRole());
    }

    @Test
    public void testUpdateAccount() {

        UserDTO userDTO1 = UserDTO.builder()
                .id(ID)
                .username(USERNAME1)
                .firstName(FIRST_NAME1)
                .lastName(LAST_NAME1)
                .email(EMAIL1)
                .address(ADDRESS1)
                .build();

        userService.updateAccount(userDTO1);

        assertTrue(userService.getUsers().size() >= 1);

        UserDTO expectedUserDTO = userService.getUsers().get(0);
        assertEquals(expectedUserDTO.getUsername(), userDTO1.getUsername());
        assertEquals(expectedUserDTO.getFirstName(), userDTO1.getFirstName());
        assertEquals(expectedUserDTO.getLastName(), userDTO1.getLastName());
        assertEquals(expectedUserDTO.getAddress(), userDTO1.getAddress());
        assertEquals(expectedUserDTO.getEmail(), userDTO1.getEmail());
    }
}

