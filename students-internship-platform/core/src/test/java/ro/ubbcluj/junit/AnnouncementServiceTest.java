package ro.ubbcluj.junit;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ro.ubbcluj.converter.AnnouncementConverter;
import ro.ubbcluj.dto.AnnouncementDTO;
import ro.ubbcluj.entity.Account;
import ro.ubbcluj.entity.Announcement;
import ro.ubbcluj.entity.Person;
import ro.ubbcluj.entity.Role;
import ro.ubbcluj.enums.RoleEnum;
import ro.ubbcluj.interfaces.AnnouncementService;
import ro.ubbcluj.repository.AccountRepository;
import ro.ubbcluj.repository.AnnouncementRepository;
import ro.ubbcluj.service.AnnouncementServiceImpl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * This class contains junit tests for Announcement Service
 */
@RunWith(MockitoJUnitRunner.class)
public class AnnouncementServiceTest {
    private static final String ANNOUNCEMENT_NAME1 = "Plaza";
    private static final String ANNOUNCEMENT_ADDRESS1 = "Cluj";
    private static final String ANNOUNCEMENT_NAME2 = "Hilton";
    private static final String ANNOUNCEMENT_ADDRESS2 = "Oradea";
    private static final long ID1 = 1L;
    private static final long ID2 = 2L;
    private static final String ANNOUNCEMENT_DESCRIPTION = "Is nice announcement";
    private static final String ANNOUNCEMENT_TECHNOLOGIES = "Technologies";
    private static final long TIMESTAMP1 = 1507059800L;
    private static final long TIMESTAMP2 = 1508059800L;
    private static final Date DATE1 = new Date(TIMESTAMP1 * 1000);
    private static final Date DATE2 = new Date(TIMESTAMP2 * 1000);

    private static final String PERSON_NAME1 = "a";
    private static final String PERSON_NAME2 = "b";

    private static final long ANNOUNCEMENT_FACILITY_ID = 1L;
    private static final String PICTURE = "p1";

    private static final String USERNAME = "user";
    @InjectMocks
    private static final AnnouncementService announcementService = new AnnouncementServiceImpl();
    @Mock
    private static AnnouncementRepository announcementRepository;
    @Mock
    private static AccountRepository accountRepository;

    /**
     * Method used to mock announcement entities.
     *
     * @return list of announcements
     */
    private static List<Announcement> createAnnouncements() {
        Person person1 = Person.builder()
                .firstName(PERSON_NAME1)
                .role(new Role(RoleEnum.COMPANY))
                .account(createAccounts().get(0))
                .build();

        Person person2 = Person.builder()
                .firstName(PERSON_NAME2)
                .role(new Role(RoleEnum.COMPANY))
                .account(createAccounts().get(1))
                .build();

        Announcement firstAnnouncementEntity = Announcement.builder()
                .id(ID1)
                .title(ANNOUNCEMENT_NAME1)
                .person(person1)
                .description(ANNOUNCEMENT_DESCRIPTION)
                .technologies(ANNOUNCEMENT_TECHNOLOGIES)
                .startDate(DATE1)
                .endDate(DATE2)
                .build();

        Announcement secondAnnouncementEntity = Announcement.builder()
                .id(ID1)
                .title(ANNOUNCEMENT_NAME2)
                .person(person2)
                .description(ANNOUNCEMENT_DESCRIPTION)
                .technologies(ANNOUNCEMENT_TECHNOLOGIES)
                .startDate(DATE1)
                .endDate(DATE2)
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
                .active(true)
                .registrationDate(new Date())
                .build();

        Account account1 = Account.builder()
                .id(ID1)
                .username(USERNAME)
                .active(false)
                .registrationDate(new Date())
                .build();

        return Arrays.asList(account, account1);
    }

    @Test
    public void testGetAllAnnouncements() {
        // given
        List<Announcement> announcements = createAnnouncements();

        // when
        when(announcementRepository.findAllByActive(true))
                .thenReturn(announcements);

        // call
        List<AnnouncementDTO> announcementDTOList = announcementService.getAllAnnouncements();

        // then
        assertNotNull(announcementDTOList); // not null
        assertEquals(announcements.size(), announcementDTOList.size());
        assertEquals(announcements.get(0).getTitle(), announcementDTOList.get(0).getTitle());
        assertEquals(announcements.get(0).getDescription(), announcementDTOList.get(0).getDescription());
        assertEquals(announcements.get(0).getStartDate(), announcementDTOList.get(0).getStartDate());
        assertEquals(announcements.get(0).getEndDate(), announcementDTOList.get(0).getEndDate());
        assertEquals(announcements.get(0).getTechnologies(), announcementDTOList.get(0).getTechnologies());
    }

    @Test
    public void testAnnouncementDTOConversion() {
        Announcement announcement = Announcement.builder()
                .title(ANNOUNCEMENT_NAME1)
                .person(new Person())
                .description(ANNOUNCEMENT_DESCRIPTION)
                .technologies(ANNOUNCEMENT_TECHNOLOGIES)
                .startDate(DATE1)
                .endDate(DATE2)
                .build();

        AnnouncementDTO announcementDTO = AnnouncementConverter.convertToDTO(announcement);

        assertNotNull(announcement);
        assertNotNull(announcementDTO);
        assertEquals(ANNOUNCEMENT_NAME1, announcementDTO.getTitle());
        assertEquals(ANNOUNCEMENT_DESCRIPTION, announcementDTO.getDescription());
        assertEquals(DATE1, announcementDTO.getStartDate());
        assertEquals(DATE2, announcementDTO.getEndDate());
        assertEquals(ANNOUNCEMENT_TECHNOLOGIES, announcementDTO.getTechnologies());
    }

    @Test
    public void testCreateAnnouncement() {
        // given
        AnnouncementDTO announcementDTO = AnnouncementDTO.builder()
                .title(ANNOUNCEMENT_NAME1)
                .description(ANNOUNCEMENT_DESCRIPTION)
                .technologies(ANNOUNCEMENT_TECHNOLOGIES)
                .startDate(DATE1)
                .endDate(DATE2)
                .build();

        List<Announcement> announcements = createAnnouncements();

        // when
        Announcement announcement = announcements.get(0);
        when(announcementRepository.findOne(ID1))
                .thenReturn(announcement);

        when(announcementRepository.save(any(Announcement.class))).thenReturn(announcement);

        // call
        announcementService.createAnnouncement(announcementDTO);

        // then
        assertEquals(announcementDTO.getTitle(), announcement.getTitle());
        assertEquals(announcementDTO.getTechnologies(), announcement.getTechnologies());
        assertEquals(announcementDTO.getDescription(), announcement.getDescription());
        assertEquals(announcementDTO.getStartDate(), announcement.getStartDate());
        assertEquals(announcementDTO.getEndDate(), announcement.getEndDate());
    }

    @Test
    public void testDeleteAnnouncement() {
        // given
        AnnouncementDTO announcementDTO = AnnouncementDTO.builder()
                .id(ID1)
                .title(ANNOUNCEMENT_NAME1)
                .description(ANNOUNCEMENT_DESCRIPTION)
                .technologies(ANNOUNCEMENT_TECHNOLOGIES)
                .startDate(DATE1)
                .endDate(DATE2)
                .build();

        List<Announcement> announcements = createAnnouncements();

        // when
        when(announcementRepository.findOne(ID1)).thenReturn(announcements.get(0));
        when(accountRepository.save(createAccounts().get(0))).thenReturn(createAccounts().get(1));
        when(announcementRepository.save(any(Announcement.class))).thenReturn(announcements.get(0));

        // call
        AnnouncementDTO savedAnnouncement = announcementService.createAnnouncement(announcementDTO);
        announcementService.deleteAnnouncement(savedAnnouncement.getId());

        AnnouncementDTO announcementDTO1 = announcementService.findAnnouncementById(ID1);

        //then
        assertEquals(false, announcementDTO1.isActive());
    }

    @Test
    public void testUpdateAnnouncement() {

        // given
        AnnouncementDTO announcementDTO = AnnouncementDTO.builder()
                .id(ID1)
                .title(ANNOUNCEMENT_NAME1)
                .description(ANNOUNCEMENT_DESCRIPTION)
                .technologies(ANNOUNCEMENT_TECHNOLOGIES)
                .startDate(DATE1)
                .endDate(DATE2)
                .build();

        Announcement initialAnnouncement = createAnnouncements().get(0);

        when(announcementRepository.findOne(ID1))
                .thenReturn(initialAnnouncement);

        announcementService.updateAnnouncement(announcementDTO);

        assertEquals(ANNOUNCEMENT_NAME1, announcementDTO.getTitle());
        assertEquals(ANNOUNCEMENT_DESCRIPTION, announcementDTO.getDescription());
        assertEquals(DATE1, announcementDTO.getStartDate());
        assertEquals(DATE2, announcementDTO.getEndDate());
        assertEquals(ANNOUNCEMENT_TECHNOLOGIES, announcementDTO.getTechnologies());
    }

}
