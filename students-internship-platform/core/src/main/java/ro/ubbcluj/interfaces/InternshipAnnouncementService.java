package ro.ubbcluj.interfaces;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ro.ubbcluj.dto.InternshipAnnouncementDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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


//    Page<InternshipAnnouncementDTO> findAnnouncementsByAnyField(String location,
//                                                                Date postingDate,
//                                                                Date startingDate,
//                                                                Date endDate,
//                                                                String domains,
//                                                                boolean paidOrNot,
//                                                                String duration,
//                                                                String workingTime,
//                                                                Pageable pageable);
        List<InternshipAnnouncementDTO> findAnnouncementsByAnyField(String location,
                                                                Date postingDate,
                                                                Date startingDate,
                                                                Date endDate,
                                                                String domains,
                                                                boolean paidOrNot,
                                                                String duration,
                                                                String workingTime);

}
