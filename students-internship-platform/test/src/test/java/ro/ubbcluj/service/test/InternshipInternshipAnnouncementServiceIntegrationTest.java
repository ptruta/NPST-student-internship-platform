package ro.ubbcluj.service.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ro.ubbcluj.dto.InternshipAnnouncementDTO;
import ro.ubbcluj.interfaces.InternshipAnnouncementService;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.DateUtil.now;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * This class tests all flows from Announcement Service
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@SqlGroup({@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        scripts = "classpath:drop-create.sql")})
public class InternshipInternshipAnnouncementServiceIntegrationTest {

    private static final String ANNOUNCEMENT_NAME = "Hilton";
    private static final Long ID = 1L;
    @Autowired
    private InternshipAnnouncementService internshipAnnouncementService;

    @Test
    public void testAnnouncementService() {
        assertNotNull(internshipAnnouncementService);
    }

    @Test
    public void testGetAllAnnouncementsNotNull() {
        final List<InternshipAnnouncementDTO> internshipAnnouncementDTOList = internshipAnnouncementService.getAllAnnouncements();

        assertNotNull(internshipAnnouncementDTOList);

        for (final InternshipAnnouncementDTO internshipAnnouncementDTO : internshipAnnouncementDTOList) {
            assertNotNull(internshipAnnouncementDTO.getId());
            assertNotNull(internshipAnnouncementDTO.getTitle());
        }
    }

    @Test
    public void testCreateAnnouncement() {
        final InternshipAnnouncementDTO internshipAnnouncementDTO = InternshipAnnouncementDTO.builder()
                .id(3L)
                .title(ANNOUNCEMENT_NAME)
                .startDate(now())
                .endDate(Date.from(now().toInstant().plusSeconds(10)))
                .availability(true)
                .build();

        internshipAnnouncementService.createAnnouncement(internshipAnnouncementDTO);

        assertEquals(3, internshipAnnouncementService.getAllAnnouncements().size());
        int size = internshipAnnouncementService.getAllAnnouncements().size() - 1;
        InternshipAnnouncementDTO expectedInternshipAnnouncementDTO = internshipAnnouncementService.getAllAnnouncements().get(size);

        assertThat(expectedInternshipAnnouncementDTO).isEqualToIgnoringGivenFields(internshipAnnouncementDTO,
                "startDate", "endDate");
        assertThat(expectedInternshipAnnouncementDTO.getStartDate().toInstant())
                .isEqualTo(internshipAnnouncementDTO.getStartDate().toInstant());
        assertThat(expectedInternshipAnnouncementDTO.getEndDate().toInstant())
                .isEqualTo(internshipAnnouncementDTO.getEndDate().toInstant());

    }

    @Test
    public void testDeleteAnnouncement() {

        internshipAnnouncementService.deleteAnnouncement(ID);

        InternshipAnnouncementDTO internshipAnnouncementDTO = internshipAnnouncementService.findAnnouncementById(ID);

        assertEquals(false, internshipAnnouncementDTO.isAvailability());
    }
}
