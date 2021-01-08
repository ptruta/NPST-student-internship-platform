package ro.ubbcluj.interfaces;

import ro.ubbcluj.dto.AnnouncementDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AnnouncementService {

    List<AnnouncementDTO> getAllAnnouncements();

    AnnouncementDTO findAnnouncementById(Long id);

    AnnouncementDTO updateAnnouncement(AnnouncementDTO announcementDTO);

    AnnouncementDTO createAnnouncement(AnnouncementDTO announcementDTO);

    Page<AnnouncementDTO> getAllAnnouncements(Pageable pageable);

    Page<AnnouncementDTO> findAnnouncementsByName(String name, Pageable pageable);

    AnnouncementDTO createAnnouncementDTO(Long id);

    void deleteAnnouncement(Long id);

    Page<AnnouncementDTO> findAnnouncementByCompany(String username, Pageable pageable);

    Long findAnnouncementIdByManager(String username);

    void applyForAnnouncement(String username, Long announcementId);
}
