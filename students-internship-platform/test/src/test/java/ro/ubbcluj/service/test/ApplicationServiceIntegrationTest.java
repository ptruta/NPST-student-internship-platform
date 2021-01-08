package ro.ubbcluj.service.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ro.ubbcluj.dto.ApplicationDTO;
import ro.ubbcluj.interfaces.ApplicationService;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * This class tests all flows from Application Service
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@SqlGroup({@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        scripts = "classpath:drop-create.sql")})
public class ApplicationServiceIntegrationTest {

    private static final long ID1 = 1L;
    private static final long ID2 = 2L;
    private static final long TIMESTAMP1 = 1507059800L;
    private static final long TIMESTAMP2 = 1508059800L;
    private static final long TIMESTAMP3 = 1505648453L;
    private static final long TIMESTAMP4 = 1505734853L;
    private static final Date DATE1 = new Date(TIMESTAMP1 * 1000);
    private static final Date DATE2 = new Date(TIMESTAMP2 * 1000);
    private static final Date DATE3 = new Date(TIMESTAMP3 * 1000);
    private static final Date DATE4 = new Date(TIMESTAMP4 * 1000);
    private static final double TOTAL_PRICE1 = 1200.0;
    private static final String USERNAME = "myUser";

    @Autowired
    private ApplicationService applicationService;

    @Test
    public void testApplicationService() {
        assertNotNull(applicationService);
    }

    @Test
    public void testGetAllApplicationsNotNull() {
        final List<ApplicationDTO> applicationDTOList = applicationService.getApplications();

        assertNotNull(applicationDTOList);
        assertTrue(applicationDTOList.size() >= 1);

        checkAttributesList(applicationDTOList);
    }

    @Test
    public void testGetAllApplicationsByAnnouncementNotNull() {
        final List<ApplicationDTO> applicationDTOList = applicationService.getApplicationsByAnnouncement(ID1);

        assertNotNull(applicationDTOList);
        assertEquals(2, applicationDTOList.size());

        checkAttributesList(applicationDTOList);

        final List<ApplicationDTO> applicationDTOList1 = applicationService.getApplicationsByAnnouncement(ID2);

        assertNotNull(applicationDTOList1);
        assertEquals(4, applicationDTOList1.size());
    }

    @Test
    public void testGetAllApplicationsByUsername() {
        final List<ApplicationDTO> applicationDTOList = applicationService.getApplicationsByUsername(USERNAME);

        assertNotNull(applicationDTOList);
        assertEquals(3, applicationDTOList.size());

        checkAttributesList(applicationDTOList);
    }

    @Test
    public void testGetApplicationById() {
        final ApplicationDTO applicationDTO = applicationService.getApplicationById(ID1);

        assertNotNull(applicationDTO);

        checkAttributes(applicationDTO);
    }

//    @Test
//    public void testCreateApplication() {
//
//        ApplicationDTO expectedApplicationDTO = ApplicationDTO.builder()
//                .startDate(DATE1)
//                .endDate(DATE2)
//                .announcementId(ID1)
//                .username(USERNAME)
//                .personEmail(application.getPerson().getEmail())
//                .announcementId(application.getAnnouncement().getId())
//                .title(application.getAnnouncement().getTitle())
//                .username(application.getPerson().getAccount().getUsername())
//                .personName(application.getPerson().getFirstName() + " " + application.getPerson().getLastName())
//                .userId(application.getPerson().getId())
//                .startDate(application.getAnnouncement().getStartDate())
//                .endDate(application.getAnnouncement().getEndDate())
//                .companyName(application.getAnnouncement().getPerson().getFirstName() + " "
//                        + application.getAnnouncement().getPerson().getLastName())
//                .build();
//
//        ApplicationDTO applicationDTO = applicationService.createApplication(expectedApplicationDTO);
//        checkAttributes(applicationDTO);
//
//        assertEquals(expectedApplicationDTO.getStartDate(), applicationDTO.getStartDate());
//        assertEquals(expectedApplicationDTO.getEndDate(), applicationDTO.getEndDate());
//        assertEquals(expectedApplicationDTO.getAnnouncementId(), applicationDTO.getAnnouncementId());
//        assertEquals(expectedApplicationDTO.getUserId(), applicationDTO.getUserId());
//    }

    /**
     * Method used to check if attributes are not null in a list
     *
     * @param applicationDTOList
     */
    private void checkAttributesList(List<ApplicationDTO> applicationDTOList) {
        for (final ApplicationDTO applicationDTO : applicationDTOList) {
            checkAttributes(applicationDTO);
        }
    }

    /**
     * Method used to check if attributes of a applicationDTO are not null
     *
     * @param applicationDTO
     */
    private void checkAttributes(ApplicationDTO applicationDTO) {
        assertNotNull(applicationDTO.getId());
        assertNotNull(applicationDTO.getStartDate());
        assertNotNull(applicationDTO.getEndDate());
        assertNotNull(applicationDTO.getTitle());
    }
}
