package ro.ubbcluj.junit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ro.ubbcluj.dto.ApplicationDTO;
import ro.ubbcluj.entity.Account;
import ro.ubbcluj.entity.Announcement;
import ro.ubbcluj.entity.Application;
import ro.ubbcluj.entity.Person;
import ro.ubbcluj.enums.GenderEnum;
import ro.ubbcluj.interfaces.ApplicationService;
import ro.ubbcluj.repository.AnnouncementRepository;
import ro.ubbcluj.repository.ApplicationRepository;
import ro.ubbcluj.repository.PersonRepository;
import ro.ubbcluj.service.ApplicationServiceImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * This class contains junit tests for Application Service
 */
@RunWith(MockitoJUnitRunner.class)
public class ApplicationServiceTest {

    private static final String EMAIL1 = "email1";
    private static final String FIRST_NAME1 = "First1";
    private static final String LAST_NAME1 = "Last1";
    private static final String ANNOUNCEMENT_NAME1 = "Junior";
    private static final String ANNOUNCEMENT_NAME2 = "Senior";
    private static final String DESCRIPTION = "Description";
    private static final String TECHNOLOGIES = "Technologies";
    private static final long ID1 = 1L;
    private static final long ID2 = 2L;
    private static final long ID3 = 3L;
    private static final long TIMESTAMP1 = 1507059800L;
    private static final long TIMESTAMP2 = 1508059800L;
    private static final long TIMESTAMP3 = 1505648453L;
    private static final long TIMESTAMP4 = 1505734853L;
    private static final Date DATE1 = new Date(TIMESTAMP1 * 1000);
    private static final Date DATE2 = new Date(TIMESTAMP2 * 1000);
    private static final Date DATE3 = new Date(TIMESTAMP3 * 1000);
    private static final Date DATE4 = new Date(TIMESTAMP4 * 1000);
    private static final String USERNAME = "user1";
    private static final String PASSWORD = "user1";
    @InjectMocks
    private static final ApplicationService applicationService = new ApplicationServiceImpl();
    @Mock
    private static ApplicationRepository applicationRepository;
    @Mock
    private static PersonRepository personRepository;
    @Mock
    private static AnnouncementRepository announcementRepository;

    /**
     * Method used to mock application entities.
     *
     * @return list of applications
     */
    private static List<Application> createApplications() {

        Application firstApplicationEntity = Application.builder()
                .id(ID1)
                .person(createPeople().get(0))
                .announcement(createAnnouncements().get(0))
                .build();

        Application secondApplicationEntity = Application.builder()
                .id(ID2)
                .person(createPeople().get(0))
                .announcement(createAnnouncements().get(1))
                .build();

        return Arrays.asList(firstApplicationEntity, secondApplicationEntity);
    }

    /**
     * Method used to mock person entities.
     *
     * @return list of person
     */
    private static List<Person> createPeople() {

        Date date1 = new Date();

        Person firstPersonEntity = Person.builder()
                .id(ID1)
                .birthDate(date1)
                .email(EMAIL1)
                .firstName(FIRST_NAME1)
                .lastName(LAST_NAME1)
                .gender(GenderEnum.FEMALE)
                .account(createAccounts().get(0))
                .build();

        return Collections.singletonList(firstPersonEntity);
    }

    /**
     * Method used to mock announcement entities.
     *
     * @return list of announcements
     */
    private static List<Announcement> createAnnouncements() {

        Announcement firstAnnouncementEntity = Announcement.builder()
                .id(ID1)
                .title(ANNOUNCEMENT_NAME1)
                .description(DESCRIPTION)
                .technologies(TECHNOLOGIES)
                .startDate(DATE1)
                .endDate(DATE2)
                .deadline(DATE3)
                .person(createPeople().get(0))
                .build();

        Announcement secondAnnouncementEntity = Announcement.builder()
                .id(ID1)
                .title(ANNOUNCEMENT_NAME2)
                .description(DESCRIPTION)
                .technologies(TECHNOLOGIES)
                .startDate(DATE1)
                .endDate(DATE2)
                .deadline(DATE3)
                .person(createPeople().get(0))
                .build();

        return Arrays.asList(firstAnnouncementEntity, secondAnnouncementEntity);
    }

    /**
     * Method used to mock account entities.
     *
     * @return list of accounts
     */
    private static List<Account> createAccounts() {

        Account account = Account.builder()
                .id(ID1)
                .username(USERNAME)
                .password(PASSWORD)
                .active(true)
                .registrationDate(new Date())
                .build();

        return Collections.singletonList(account);
    }

    @Test
    public void testGetAllApplications() {
        // given
        List<Application> applications = createApplications();

        // when
        when(applicationRepository.findAll()).thenReturn(applications);

        // call
        List<ApplicationDTO> applicationDTOList = applicationService.getApplications();

        // then
        assertNotNull(applicationDTOList); // not null
        assertEquals(applications.size(), applicationDTOList.size());
    }

    @Test
    public void testGetAllApplicationsByAnnouncement() {
        // given
        List<Application> applications = Collections.singletonList(createApplications().get(0));

        // when
        when(applicationRepository.findAllByAnnouncement_Id(ID1))
                .thenReturn(applications);

        // call
        List<ApplicationDTO> applicationDTOList = applicationService.getApplicationsByAnnouncement(ID1);

        // then
        assertNotNull(applicationDTOList); // not null
        assertEquals(applications.size(), applicationDTOList.size());
    }

    @Test
    public void testGetAllApplicationsByUsername() {
        // given
        List<Application> applications = Collections.singletonList(createApplications().get(0));

        // when
        when(applicationRepository.findAllByPerson_Account_Username(USERNAME))
                .thenReturn(applications);

        // call
        List<ApplicationDTO> applicationDTOList = applicationService.getApplicationsByUsername(USERNAME);

        // then
        assertNotNull(applicationDTOList); // not null
        assertEquals(applications.size(), applicationDTOList.size());
    }

    @Test
    public void testCreateApplication() {
        ApplicationDTO applicationDTO = ApplicationDTO.builder()
                .startDate(DATE1)
                .endDate(DATE2)
                .announcementId(ID1)
                .username(USERNAME)
                .build();

        //when
        when(personRepository.findByAccount_Username(USERNAME))
                .thenReturn(createPeople().get(0));

        when(announcementRepository.findOne(applicationDTO.getAnnouncementId()))
                .thenReturn(createAnnouncements().get(0));

        Application application = createApplications().get(0);
        when(applicationRepository.findAll()).thenReturn(Collections.singletonList(application));

        applicationService.createApplication(applicationDTO);

        //call
        List<ApplicationDTO> applications = applicationService.getApplications();
        ApplicationDTO expectedApplicationDTO = applications.get(0);

        //then
        assertTrue(expectedApplicationDTO.getStartDate().equals(applicationDTO.getStartDate()));
        assertTrue(expectedApplicationDTO.getEndDate().equals(applicationDTO.getEndDate()));
        assertEquals(expectedApplicationDTO.getAnnouncementId(), applicationDTO.getAnnouncementId());
    }

    @Test
    public void testGetApplicationById() {
        ApplicationDTO expectedApplicationDTO = ApplicationDTO.builder()
                .id(ID1)
                .startDate(DATE1)
                .endDate(DATE2)
                .build();

        //when
        when(applicationRepository.findOne(ID1))
                .thenReturn(createApplications().get(0));

        //call
        ApplicationDTO applicationDTO = applicationService.getApplicationById(ID1);

        //then
        assertEquals(expectedApplicationDTO.getStartDate(), applicationDTO.getStartDate());
        assertEquals(expectedApplicationDTO.getEndDate(), applicationDTO.getEndDate());
    }

}
