package ro.ubbcluj.junit;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ro.ubbcluj.converter.InternshipAnnouncementConverter;
import ro.ubbcluj.dto.InternshipAnnouncementDTO;
import ro.ubbcluj.entity.Account;
import ro.ubbcluj.entity.InternshipAnnouncement;
import ro.ubbcluj.entity.Role;
import ro.ubbcluj.entity.UserAuthentication;
import ro.ubbcluj.enums.RoleEnum;
import ro.ubbcluj.interfaces.InternshipAnnouncementService;
import ro.ubbcluj.repository.AccountRepository;
import ro.ubbcluj.repository.InternshipAnnouncementRepository;
import ro.ubbcluj.service.InternshipAnnouncementServiceImpl;

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
public class InternshipInternshipAnnouncementServiceTest {
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
    private static final InternshipAnnouncementService INTERNSHIP_ANNOUNCEMENT_SERVICE = new InternshipAnnouncementServiceImpl();
    @Mock
    private static InternshipAnnouncementRepository internshipAnnouncementRepository;
    @Mock
    private static AccountRepository accountRepository;

    /**
     * Method used to mock announcement entities.
     *
     * @return list of announcements
     */
    private static List<InternshipAnnouncement> createAnnouncements() {
        UserAuthentication UserAuthentication1 = UserAuthentication.builder()
                .firstName(PERSON_NAME1)
                .role(new Role(RoleEnum.RECRUITER))
                .account(createAccounts().get(0))
                .build();

        UserAuthentication UserAuthentication2 = UserAuthentication.builder()
                .firstName(PERSON_NAME2)
                .role(new Role(RoleEnum.RECRUITER))
                .account(createAccounts().get(1))
                .build();

        InternshipAnnouncement firstInternshipAnnouncementEntity = InternshipAnnouncement.builder()
                .id(ID1)
                .title(ANNOUNCEMENT_NAME1)
                .userAuthentication(UserAuthentication1)
                .startDate(DATE1)
                .endDate(DATE2)
                .build();

        InternshipAnnouncement secondInternshipAnnouncementEntity = InternshipAnnouncement.builder()
                .id(ID1)
                .title(ANNOUNCEMENT_NAME2)
                .userAuthentication(UserAuthentication2)
                .startDate(DATE1)
                .endDate(DATE2)
                .build();

        return Arrays.asList(firstInternshipAnnouncementEntity, secondInternshipAnnouncementEntity);
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
        List<InternshipAnnouncement> internshipAnnouncements = createAnnouncements();

        // when
        when(internshipAnnouncementRepository.findAllByAvailability(true))
                .thenReturn(internshipAnnouncements);

        // call
        List<InternshipAnnouncementDTO> internshipAnnouncementDTOList = INTERNSHIP_ANNOUNCEMENT_SERVICE.getAllAnnouncements();

        // then
        assertNotNull(internshipAnnouncementDTOList); // not null
        assertEquals(internshipAnnouncements.size(), internshipAnnouncementDTOList.size());
        assertEquals(internshipAnnouncements.get(0).getTitle(), internshipAnnouncementDTOList.get(0).getTitle());
        assertEquals(internshipAnnouncements.get(0).getStartDate(), internshipAnnouncementDTOList.get(0).getStartDate());
        assertEquals(internshipAnnouncements.get(0).getEndDate(), internshipAnnouncementDTOList.get(0).getEndDate());
    }

    @Test
    public void testAnnouncementDTOConversion() {
        InternshipAnnouncement internshipAnnouncement = InternshipAnnouncement.builder()
                .title(ANNOUNCEMENT_NAME1)
                .userAuthentication(new UserAuthentication())
                .startDate(DATE1)
                .endDate(DATE2)
                .build();

        InternshipAnnouncementDTO internshipAnnouncementDTO = InternshipAnnouncementConverter.convertToDTO(internshipAnnouncement);

        assertNotNull(internshipAnnouncement);
        assertNotNull(internshipAnnouncementDTO);
        assertEquals(ANNOUNCEMENT_NAME1, internshipAnnouncementDTO.getTitle());
        assertEquals(DATE1, internshipAnnouncementDTO.getStartDate());
        assertEquals(DATE2, internshipAnnouncementDTO.getEndDate());
    }

    @Test
    public void testCreateAnnouncement() {
        // given
        InternshipAnnouncementDTO internshipAnnouncementDTO = InternshipAnnouncementDTO.builder()
                .title(ANNOUNCEMENT_NAME1)
                .startDate(DATE1)
                .endDate(DATE2)
                .build();

        List<InternshipAnnouncement> internshipAnnouncements = createAnnouncements();

        // when
        InternshipAnnouncement internshipAnnouncement = internshipAnnouncements.get(0);
        when(internshipAnnouncementRepository.findOne(ID1))
                .thenReturn(internshipAnnouncement);

        when(internshipAnnouncementRepository.save(any(InternshipAnnouncement.class))).thenReturn(internshipAnnouncement);

        // call
        INTERNSHIP_ANNOUNCEMENT_SERVICE.createAnnouncement(internshipAnnouncementDTO);

        // then
        assertEquals(internshipAnnouncementDTO.getTitle(), internshipAnnouncement.getTitle());
        assertEquals(internshipAnnouncementDTO.getStartDate(), internshipAnnouncement.getStartDate());
        assertEquals(internshipAnnouncementDTO.getEndDate(), internshipAnnouncement.getEndDate());
    }

    @Test
    public void testDeleteAnnouncement() {
        // given
        InternshipAnnouncementDTO internshipAnnouncementDTO = InternshipAnnouncementDTO.builder()
                .id(ID1)
                .title(ANNOUNCEMENT_NAME1)
                .startDate(DATE1)
                .endDate(DATE2)
                .build();

        List<InternshipAnnouncement> internshipAnnouncements = createAnnouncements();

        // when
        when(internshipAnnouncementRepository.findOne(ID1)).thenReturn(internshipAnnouncements.get(0));
        when(accountRepository.save(createAccounts().get(0))).thenReturn(createAccounts().get(1));
        when(internshipAnnouncementRepository.save(any(InternshipAnnouncement.class))).thenReturn(internshipAnnouncements.get(0));

        // call
        InternshipAnnouncementDTO savedAnnouncement = INTERNSHIP_ANNOUNCEMENT_SERVICE.createAnnouncement(internshipAnnouncementDTO);
        INTERNSHIP_ANNOUNCEMENT_SERVICE.deleteAnnouncement(savedAnnouncement.getId());

        InternshipAnnouncementDTO internshipAnnouncementDTO1 = INTERNSHIP_ANNOUNCEMENT_SERVICE.findAnnouncementById(ID1);

        //then
        assertEquals(false, internshipAnnouncementDTO1.isAvailability());
    }

    @Test
    public void testUpdateAnnouncement() {

        // given
        InternshipAnnouncementDTO internshipAnnouncementDTO = InternshipAnnouncementDTO.builder()
                .id(ID1)
                .title(ANNOUNCEMENT_NAME1)
                .startDate(DATE1)
                .endDate(DATE2)
                .build();

        InternshipAnnouncement initialInternshipAnnouncement = createAnnouncements().get(0);

        when(internshipAnnouncementRepository.findOne(ID1))
                .thenReturn(initialInternshipAnnouncement);

        INTERNSHIP_ANNOUNCEMENT_SERVICE.updateAnnouncement(internshipAnnouncementDTO);

        assertEquals(ANNOUNCEMENT_NAME1, internshipAnnouncementDTO.getTitle());
        assertEquals(DATE1, internshipAnnouncementDTO.getStartDate());
        assertEquals(DATE2, internshipAnnouncementDTO.getEndDate());
    }

}
