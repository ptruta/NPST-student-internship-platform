package ro.ubbcluj.service.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ro.ubbcluj.dto.AnnouncementDTO;
import ro.ubbcluj.interfaces.AnnouncementService;

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
public class AnnouncementServiceIntegrationTest {

    private static final String ANNOUNCEMENT_NAME = "Hilton";
    private static final Long ID = 1L;
    @Autowired
    private AnnouncementService announcementService;

    @Test
    public void testAnnouncementService() {
        assertNotNull(announcementService);
    }

    @Test
    public void testGetAllAnnouncementsNotNull() {
        final List<AnnouncementDTO> announcementDTOList = announcementService.getAllAnnouncements();

        assertNotNull(announcementDTOList);

        for (final AnnouncementDTO announcementDTO : announcementDTOList) {
            assertNotNull(announcementDTO.getId());
            assertNotNull(announcementDTO.getTitle());
        }
    }

    @Test
    public void testCreateAnnouncement() {
        final AnnouncementDTO announcementDTO = AnnouncementDTO.builder()
                .id(3L)
                .title(ANNOUNCEMENT_NAME)
                .startDate(now())
                .endDate(Date.from(now().toInstant().plusSeconds(10)))
                .active(true)
                .build();

        announcementService.createAnnouncement(announcementDTO);

        assertEquals(3, announcementService.getAllAnnouncements().size());
        int size = announcementService.getAllAnnouncements().size() - 1;
        AnnouncementDTO expectedAnnouncementDTO = announcementService.getAllAnnouncements().get(size);

        assertThat(expectedAnnouncementDTO).isEqualToIgnoringGivenFields(announcementDTO,
                "startDate", "endDate");
        assertThat(expectedAnnouncementDTO.getStartDate().toInstant())
                .isEqualTo(announcementDTO.getStartDate().toInstant());
        assertThat(expectedAnnouncementDTO.getEndDate().toInstant())
                .isEqualTo(announcementDTO.getEndDate().toInstant());

    }

    @Test
    public void testDeleteAnnouncement() {

        announcementService.deleteAnnouncement(ID);

        AnnouncementDTO announcementDTO = announcementService.findAnnouncementById(ID);

        assertEquals(false, announcementDTO.isActive());
    }
}
