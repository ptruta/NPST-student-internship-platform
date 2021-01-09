package ro.ubbcluj.interfaces;

import ro.ubbcluj.dto.InternshipAnnouncementDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ro.ubbcluj.entity.InternshipAnnouncement;

import java.util.Date;
import java.util.List;

public interface InternshipAnnouncementService {

    List<InternshipAnnouncementDTO> getAllAnnouncements();

    InternshipAnnouncementDTO findAnnouncementById(Long id);

    InternshipAnnouncementDTO updateAnnouncement(InternshipAnnouncementDTO internshipAnnouncementDTO);

    InternshipAnnouncementDTO createAnnouncement(InternshipAnnouncementDTO internshipAnnouncementDTO);

    Page<InternshipAnnouncementDTO> getAllAnnouncements(Pageable pageable);

    Page<InternshipAnnouncementDTO> findAnnouncementsByName(String name, Pageable pageable);

    InternshipAnnouncementDTO createAnnouncementDTO(Long id);

    void deleteAnnouncement(Long id);

    Page<InternshipAnnouncementDTO> findAnnouncementByCompany(String username, Pageable pageable);

    Long findAnnouncementIdByManager(String username);

    void applyForAnnouncement(String username, Long announcementId);

//    Page<InternshipAnnouncementDTO> findAnnouncementsByLocation(String location, Pageable pageable);
//
//    Page<InternshipAnnouncementDTO> findAnnouncementsByPostingDate(Date postingDate, Pageable pageable);

//    Page<InternshipAnnouncementDTO> findAnnouncementsByAnyField(String location,
//                                                                Date postingDate,
//                                                                Date startingDate,
//                                                                Date endDate,
//                                                                String domains,
//                                                                boolean paidOrNot,
//                                                                String duration,
//                                                                String workingTime,
//                                                                Pageable pageable);
}
